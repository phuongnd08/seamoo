package org.seamoo.webapp.server;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.seamoo.entities.League;
import org.seamoo.entities.Subject;
import org.seamoo.persistence.daos.LeagueDao;
import org.seamoo.persistence.daos.SubjectDao;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LeagueServiceImplTest {

	LeagueServiceImpl leagueServiceImpl;
	LeagueDao leagueDao;
	SubjectDao subjectDao;

	@BeforeMethod
	public void setUp() {
		leagueDao = mock(LeagueDao.class);
		subjectDao = mock(SubjectDao.class);
		leagueServiceImpl = new LeagueServiceImpl();
		leagueServiceImpl.leagueDao = leagueDao;
		leagueServiceImpl.subjectDao = subjectDao;
	}

	@Test
	public void saveShouldAssignAutoSubjectIdForLeagueBeforeMovingAhead() {
		League league = new League();
		leagueServiceImpl.save(1L, league);
		verify(leagueDao).persist(league);
		assertEquals(league.getSubjectAutoId(), new Long(1L));
	}
}
