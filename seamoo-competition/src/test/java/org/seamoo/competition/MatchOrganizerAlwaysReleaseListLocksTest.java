package org.seamoo.competition;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.seamoo.cache.memcacheImpl.MemcacheWrapperFactoryImpl;
import org.seamoo.competition.MatchOrganizer.DoWhileListsLockedRunner;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MatchOrganizerAlwaysReleaseListLocksTest extends LocalAppEngineTest {
	@Override
	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
	}

	@Test
	public void exceptionDuringExecutionDoesNotContaminateListLocks() throws TimeoutException {
		MatchOrganizer organizer = new MatchOrganizer();
		organizer.cacheWrapperFactory = new MemcacheWrapperFactoryImpl();
		organizer.initialize();
		try {
			organizer.doWhileListsLocked(new DoWhileListsLockedRunner() {

				@Override
				public boolean perform(List fullList, List notFullList) {
					// TODO Auto-generated method stub
					throw new RuntimeException();
				}
			});
		} catch (RuntimeException e) {
		}
		organizer.doWhileListsLocked(new DoWhileListsLockedRunner() {

			@Override
			public boolean perform(List fullList, List notFullList) {
				// TODO Auto-generated method stub
				return true;
			}
		});
	}

	@Override
	@AfterMethod
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

}
