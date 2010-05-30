package org.seamoo.competition;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.seamoo.competition.FakeRemoteObjectContainer.FakeRemoteObjectFactory;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.entities.question.QuestionType;
import org.seamoo.utils.TimeProvider;
import org.testng.annotations.Test;

public class MatchOrganizerStressTest {

	private static long SLEEP_UNIT = 1;

	private static long MAX_TEST_TIME = 30000;

	private static int SIMULTANEOUS_USERS_COUNT = 100;

	private Thread getTypicalMemberActionThread(final MatchOrganizer organizer, final Long memberId) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// Wait for match to be formed
				Match m;
				do {
					try {
						Thread.sleep(30 * SLEEP_UNIT);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println("Interupted while waiting for match to be formed: Member#" + memberId);
						e.printStackTrace();
					}
					m = organizer.getMatchForUser(memberId);
				} while (m.getPhase() != MatchPhase.PLAYING);

				// Submit answer
				System.out.println("Received " + m.getQuestionIds().size() + " questions");
				for (int i = 0; i < m.getQuestionIds().size(); i++) {
					try {
						Thread.sleep(30 * SLEEP_UNIT);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					try {
						if (i % 2 == 0)
							organizer.submitAnswer(memberId, i + 1, "1");
						else
							organizer.ignoreQuestion(memberId, i + 1);
					} catch (TimeoutException e) {
						System.err.println(e);
						throw new RuntimeException(e);
					}
				}

				// Wait for match to be finished
				do {
					try {
						Thread.sleep(30 * SLEEP_UNIT);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println("Interupted while waiting for match to be finished: Member#" + memberId);
						e.printStackTrace();
					}
					m = organizer.getMatchForUser(memberId);
				} while (m.getPhase() != MatchPhase.FINISHED);

				incFinishedUser(memberId);

			}
		});
		return t;
	}

	int finishedUser = 0;

	private synchronized void incFinishedUser(long userId) {
		finishedUser++;
		System.out.println("finishedUser=" + finishedUser + " Member #" + userId);
	}

	@Test
	public void matchOrganizerShouldPerformFast() throws InterruptedException {
		MatchOrganizerSettings settings = new MatchOrganizerSettings();
		settings.setMatchCountDownTime(100 * SLEEP_UNIT);
		settings.setMatchTime(120 * 10 * SLEEP_UNIT);
		settings.setMaxLockWaitTime(1000 * SLEEP_UNIT);
		settings.setCandidateActivePeriod(Long.MAX_VALUE / 2);
		// make sure
		// active period
		// is long
		// enough for
		// debugging
		// purpose
		MatchOrganizer organizer = new MatchOrganizer(1L, settings);
		organizer.matchDao = mock(MatchDao.class);
		organizer.cacheWrapperFactory = new FakeRemoteObjectFactory();
		organizer.memberDao = mock(MemberDao.class);
		when(organizer.memberDao.findByKey(anyLong())).thenAnswer(new Answer<Member>() {

			@Override
			public Member answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				Long memberAutoId = (Long) invocation.getArguments()[0];
				Member m = new Member();
				m.setAutoId(memberAutoId);
				m.setDisplayName("User #" + memberAutoId);
				return m;
			}
		});
		organizer.questionDao = mock(QuestionDao.class);
		final List<Question> questions = new ArrayList<Question>();
		for (int i = 0; i < 20; i++) {
			Question q = new Question();
			q.setType(QuestionType.MULTIPLE_CHOICES);
			MultipleChoicesQuestionRevision rev = new MultipleChoicesQuestionRevision();
			rev.addChoice(new QuestionChoice());
			q.addAndSetAsCurrentRevision(rev);
			questions.add(q);
			q.setAutoId(new Long(i + 1));
		}

		when(organizer.questionDao.getRandomQuestions(anyLong(), eq(20))).thenReturn(questions);

		when(organizer.questionDao.findByKey(anyLong())).thenAnswer(new Answer<Question>() {

			@Override
			public Question answer(InvocationOnMock invocation) throws Throwable {
				Long key = (Long) invocation.getArguments()[0];
				return questions.get((int) (key - 1));
			}
		});
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				Match m = (Match) invocation.getArguments()[0];
				m.setAutoId(1L);
				return null;
			}
		}).when(organizer.matchDao).persist((Match) any());

		Thread[] threads = new Thread[SIMULTANEOUS_USERS_COUNT];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = getTypicalMemberActionThread(organizer, new Long(i + 1));
			threads[i].start();
			try {
				Thread.sleep(SLEEP_UNIT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int sleepTime = 0;
		long start = TimeProvider.DEFAULT.getCurrentTimeStamp();

		while (true) {
			if (finishedUser >= threads.length - 1)// accept the cases where 1
				// user was left behind and
				// cannot have a match
				break;
			else {
				Thread.sleep(SLEEP_UNIT * 10);
				sleepTime += SLEEP_UNIT * 10;
				if (sleepTime >= MAX_TEST_TIME) {
					System.out.println("Time out: finishedUser =" + finishedUser);
					for (int i = 0; i < threads.length; i++) {
						if (threads[i].isAlive()) {
							System.out.println("Thread #" + i + " is alive. Interupt it!");
							threads[i].interrupt();
						}
					}
					fail("Test Timeout");
				}
			}
		}
	}
}
