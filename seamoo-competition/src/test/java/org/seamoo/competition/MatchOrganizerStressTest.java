package org.seamoo.competition;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.seamoo.cache.CacheWrapper;
import org.seamoo.cache.CacheWrapperFactory;
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
import org.testng.annotations.Test;

public class MatchOrganizerStressTest {

	private static long SLEEP_UNIT = 1;

	private static long MAX_TEST_TIME = 10000;

	private static int SIMULTANEOUS_USERS_COUNT = 30;

	public static class CountableCacheWrapper<T> implements CacheWrapper<T> {

		protected long lockCount;
		protected long lockTryTime;
		private boolean locked;
		private String key;
		private Map<String, Object> map;
		Map<String, Lock> lockMap;

		public CountableCacheWrapper(String key, Map<String, Object> map, Map<String, Lock> lockMap) {
			lockCount = 0;
			lockTryTime = 0;
			this.key = key;
			this.map = map;
			this.lockMap = lockMap;
		}

		@Override
		public T getObject() {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(SLEEP_UNIT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return (T) map.get(this.key);
		}

		@Override
		public void lock(long timeout) throws TimeoutException {
			// TODO Auto-generated method stub
			long sleep = 0;
			try {
				if (!lockMap.get(key).tryLock(timeout, TimeUnit.MILLISECONDS))
					throw new TimeoutException();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.locked = true;
			this.lockCount++;
		}

		@Override
		public void putObject(T object) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(SLEEP_UNIT * 3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.map.put(key, object);
		}

		@Override
		public void unlock() {
			// TODO Auto-generated method stub
			if (this.locked == true) {
				this.locked = false;
				this.lockMap.get(key).unlock();
			} else
				throw new IllegalStateException("Cannot unlock");
		}

	}

	public static class CountableCacheWrapperFactory implements CacheWrapperFactory {

		Map<String, Object> map;
		Map<String, Lock> lockMap;

		public CountableCacheWrapperFactory() {
			this.map = new HashMap<String, Object>();
			this.lockMap = new HashMap<String, Lock>();
		}

		@Override
		public synchronized <T> CacheWrapper<T> createCacheWrapper(Class<T> clazz, String key) {
			// TODO Auto-generated method stub
			String realKey = clazz.getName() + "@" + key;
			if (!this.lockMap.containsKey(realKey))
				this.lockMap.put(realKey, new ReentrantLock());
			return new CountableCacheWrapper<T>(realKey, map, lockMap);
		}
	}

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
						e.printStackTrace();
					}
					try {
						m = organizer.getMatchForUser(memberId);
					} catch (TimeoutException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				} while (m.getPhase() != MatchPhase.PLAYING);

				// Submit answer
				System.out.println("Received " + m.getQuestions().size() + " questions");
				for (int i = 0; i < m.getQuestions().size(); i++) {
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
						e.printStackTrace();
					}
					try {
						m = organizer.getMatchForUser(memberId);
					} catch (TimeoutException e) {
						// TODO Auto-generated catch block
						System.err.println(e);
						throw new RuntimeException(e);
					}
				} while (m.getPhase() != MatchPhase.FINISHED);

				incFinishedUser(memberId);

			}
		});
		return t;
	}

	int finishedUser = 0;

	private synchronized void incFinishedUser(long userId) {
		finishedUser++;
		System.out.println("User #" + userId + " finished");
	}

	@Test
	public void matchOrganizerShouldNotLockTooMuch() throws InterruptedException {
		MatchOrganizerSettings settings = new MatchOrganizerSettings();
		settings.setMatchCountDownTime(100 * SLEEP_UNIT);
		settings.setMatchTime(120 * 10 * SLEEP_UNIT);
		settings.setMaxLockWaitTime(300 * SLEEP_UNIT);
		settings.setCandidateActivePeriod(Long.MAX_VALUE / 2);
		// make sure
		// active period
		// is long
		// enough for
		// debugging
		// purpose
		MatchOrganizer organizer = new MatchOrganizer(1L, settings);
		organizer.matchDao = mock(MatchDao.class);
		organizer.cacheWrapperFactory = new CountableCacheWrapperFactory();
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
		List<Question> questions = new ArrayList<Question>();
		for (int i = 0; i < 20; i++) {
			Question q = new Question();
			q.setType(QuestionType.MULTIPLE_CHOICES);
			MultipleChoicesQuestionRevision rev = new MultipleChoicesQuestionRevision();
			rev.addChoice(new QuestionChoice());
			q.addAndSetAsCurrentRevision(rev);
			questions.add(q);
		}

		when(organizer.questionDao.getRandomQuestions(anyLong(), eq(20))).thenReturn(questions);

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
		long start = TimeStampProvider.getCurrentTimeMilliseconds();

		while (true) {
			if (finishedUser >= threads.length - 1)// accept the cases where 1
				// user was left behind and
				// cannot have a match
				break;
			else {
				Thread.sleep(SLEEP_UNIT * 10);
				sleepTime += SLEEP_UNIT * 10;
				if (sleepTime >= MAX_TEST_TIME) {
					for (int i = 0; i < threads.length; i++) {
						if (threads[i].isAlive())
							threads[i].interrupt();
					}
					fail("Test Timeout");
				}
			}
		}

		long end = TimeStampProvider.getCurrentTimeMilliseconds();

		CountableCacheWrapper<List> fullWaitingMatches = (CountableCacheWrapper<List>) organizer.fullWaitingMatches;
		CountableCacheWrapper<List> notFullWaitingMatches = (CountableCacheWrapper<List>) organizer.notFullWaitingMatches;

		System.out.println("notFullWaitingMatches.lockCount = " + notFullWaitingMatches.lockCount);
		System.out.println("notFullWaitingMatches.lockTryTime = " + notFullWaitingMatches.lockTryTime);
		System.out.println("end-start = " + (end - start));
		System.out.println("expected match time = " + (settings.getMatchCountDownTime() + settings.getMatchTime()));
		if (notFullWaitingMatches.lockCount > 200)
			fail("Expect <=300 lockCount on notFullWaitingMatches but got " + notFullWaitingMatches.lockCount);
	}
}
