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
import org.seamoo.test.ObjectSerializer;
import org.seamoo.utils.TimeProvider;
import org.testng.annotations.Test;

public class MatchOrganizerConcurrencyTest {

	private static long SLEEP_UNIT = 1;

	private static long TEST_PHASE_COUNT = 5;

	private static long MAX_TEST_TIME = 200000;

	public static class LockTimeControllableCacheWrapper<T> implements CacheWrapper<T> {

		private boolean locked;
		private String key;
		private Map<String, Object> map;
		private Map<String, Lock> lockMap;
		private long putTime;
		private long lockTime;
		private long getTime;

		public LockTimeControllableCacheWrapper(String key, Map<String, Object> map, Map<String, Lock> lockMap,
				LockTimeControllableCacheWrapperFactory factory) {
			this.key = key;
			this.map = map;
			this.lockMap = lockMap;
			this.putTime = factory.putTime;
			this.getTime = factory.getTime;
			this.lockTime = factory.lockTime;
		}

		@Override
		public T getObject() {
			// TODO Auto-generated method stub
			System.out.println("getObject(" + key + "): sleep=" + getTime);
			try {
				Thread.sleep(getTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object o = map.get(this.key);
			if (o == null)
				return null;
			try {
				return (T) ObjectSerializer.cloneObject(o);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void lock(long timeout) throws TimeoutException {
			// TODO Auto-generated method stub
			long sleep = 0;
			try {
				if (!lockMap.get(this.key).tryLock(timeout, TimeUnit.MILLISECONDS))
					throw new TimeoutException();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e1);
			}
			try {
				System.out.println("lock(" + key + "): sleep=" + lockTime);
				Thread.sleep(lockTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void putObject(T object) {
			// TODO Auto-generated method stub
			try {
				System.out.println("putObject(" + key + "): sleep=" + putTime);
				Thread.sleep(putTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.map.put(key, object);
		}

		@Override
		public void unlock() {
			// TODO Auto-generated method stub
			System.out.println("unlock(" + key + "): nosleep (would=)" + lockTime);
			lockMap.get(key).unlock();
		}

		@Override
		public void resetLock() {
			// TODO Auto-generated method stub
			
		}

	}

	public static class LockTimeControllableCacheWrapperFactory implements CacheWrapperFactory {
		Map<String, Object> map;
		Map<String, Lock> lockMap;
		long lockTime = 100;
		long putTime = 1;
		long getTime = 1;

		public LockTimeControllableCacheWrapperFactory() {
			this.map = new HashMap<String, Object>();
			this.lockMap = new HashMap<String, Lock>();
		}

		@Override
		public synchronized <T> CacheWrapper<T> createCacheWrapper(Class<T> clazz, String key) {
			// TODO Auto-generated method stub
			String realKey = clazz.getName() + "@" + key;
			if (!this.lockMap.containsKey(realKey))
				this.lockMap.put(realKey, new ReentrantLock());
			return new LockTimeControllableCacheWrapper<T>(realKey, map, lockMap, this);
		}

	}

	interface DoAfterMatchFormed {
		public void perform(MatchOrganizer organizer, Long memberId);
	}

	private Thread getTypicalMemberActionThread(final MatchOrganizer organizer, final Long memberId,
			final DoAfterMatchFormed doAfter) {
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				// Wait for match to be formed
				Match m;
				do {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						m = organizer.getMatchForUser(memberId);
					} catch (TimeoutException e) {
						// TODO Auto-generated catch block
						throw new RuntimeException(e);
					}
				} while (m.getPhase() != MatchPhase.PLAYING);
				// Submit answer
				System.out.println("Typical job done. Received " + m.getQuestions().size() + " questions for User#" + memberId);
				doAfter.perform(organizer, memberId);
			}
		});
		return t;
	}

	int competitorCount = 0;
	Match m;

	private synchronized void incCompetitorCount() {
		competitorCount++;
	}

	private void checkIfConcurrentRequestWipeOutUserAnswers() throws InterruptedException {
		m = null;
		competitorCount = 0;
		MatchOrganizerSettings settings = new MatchOrganizerSettings();
		settings.setMatchCountDownTime(100);
		settings.setMatchTime(5000);
		settings.setMaxLockWaitTime(1000);
		settings.setCandidateActivePeriod(Long.MAX_VALUE / 2);
		// make sure
		// active period
		// is long
		// enough for
		// debugging
		// purpose
		MatchOrganizer organizer = new MatchOrganizer(1L, settings);
		final LockTimeControllableCacheWrapperFactory cacheFactory = new LockTimeControllableCacheWrapperFactory();
		cacheFactory.lockTime = 0;
		cacheFactory.putTime = 0;
		cacheFactory.getTime = 0;
		organizer.matchDao = mock(MatchDao.class);
		organizer.cacheWrapperFactory = cacheFactory;
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

		// simulate this scenario: Member#1 submit answer. While his answer is
		// being waited for persisted in data
		// Member#2 arrive. He get the match information. Then organizer get the
		// match information, check for phase.
		// Member#1 answer is persisted to datastore
		// Organizer save the match phase.-> Member#1 answer disappear, as of
		// commit #a890ab509f98e4b31c27d7cbb8e80f0c4f0e6bf3
		Thread thread1 = getTypicalMemberActionThread(organizer, 1L, new DoAfterMatchFormed() {

			@Override
			public void perform(MatchOrganizer organizer, Long memberId) {
				// TODO Auto-generated method stub
				incCompetitorCount();
				while (competitorCount != 2)
					try {
						Thread.sleep(1);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} // Wait for another competitor to catch up
				cacheFactory.lockTime = 1;
				cacheFactory.getTime = 1;
				cacheFactory.putTime = 101;
				try {
					organizer.submitAnswer(memberId, 1, "1");
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Thread thread2 = getTypicalMemberActionThread(organizer, 2L, new DoAfterMatchFormed() {

			@Override
			public void perform(MatchOrganizer organizer, Long memberId) {
				// TODO Auto-generated method stub
				incCompetitorCount();
				while (competitorCount != 2)
					try {
						Thread.sleep(1);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} // Wait for another competitor to catch up

				try {
					Thread.sleep(50);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				cacheFactory.lockTime = 102;
				cacheFactory.getTime = 2;
				cacheFactory.putTime = 202;
				try {
					m = organizer.getMatchForUser(memberId);
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread1.start();
		Thread.sleep(50L);
		thread2.start();
		int sleepTime = 0;
		long start = TimeProvider.DEFAULT.getCurrentTimeStamp();

		while (true) {
			if (m != null)
				break;
			else {
				Thread.sleep(SLEEP_UNIT * 10);
				sleepTime += SLEEP_UNIT;
				if (sleepTime >= MAX_TEST_TIME) {
					thread1.interrupt();
					thread2.interrupt();
					fail("Test Take Too Much Time. Expect Test Time <" + MAX_TEST_TIME);
				}
			}
		}

		long end = TimeProvider.DEFAULT.getCurrentTimeStamp();
		assertEquals(m.getCompetitors().get(0).getAnswers().size(), 1);
	}

	@Test
	public void concurrentRequestShouldNotWipeOutUserAnswers() throws InterruptedException {
		// since the test is not of high reliability, run many times to be sure;
		for (int i = 0; i < TEST_PHASE_COUNT; i++) {
			System.out.println("CONCURRENT TEST PHASE #" + i);
			checkIfConcurrentRequestWipeOutUserAnswers();
		}
	}
}
