package org.seamoo.daos.twigImpl;

import static org.testng.Assert.*;

import java.util.List;

import org.seamoo.entities.LeagueMembership;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TwigLeagueMembershipDaoImplTest extends LocalAppEngineTest {

	@Override
	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
	}

	@Override
	@AfterMethod
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	private LeagueMembership getSampleLMS(long memberAutoId, long leagueAutoId, int year, int month) {
		LeagueMembership lms = new LeagueMembership();
		lms.setMemberAutoId(memberAutoId);
		lms.setLeagueAutoId(leagueAutoId);
		lms.setYear(year);
		lms.setMonth(month);
		return lms;
	}

	@Test
	public void getByMemberAndLeagueShouldReturnMatchingMembership() {
		LeagueMembership lms = getSampleLMS(1L, 2L, 2010, 5);
		TwigLeagueMembershipDaoImpl daoImpl = new TwigLeagueMembershipDaoImpl();
		daoImpl.persist(lms);
		LeagueMembership reloadedLms = daoImpl.findByMemberAndLeagueAndMoment(1L, 2L, 2010, 5);
		assertNotNull(reloadedLms);
	}

	@Test
	public void findUndeterminedByMinimumAutoIdAndMomentShouldReturnRecordsInASCOrderOfAutoId() {
		TwigLeagueMembershipDaoImpl daoImpl = new TwigLeagueMembershipDaoImpl();
		LeagueMembership[] lmses = new LeagueMembership[] { getSampleLMS(1L, 2L, 2010, 5), getSampleLMS(1L, 3L, 2010, 5),
				getSampleLMS(1L, 4L, 2010, 5) };
		daoImpl.persist(lmses);
		List<LeagueMembership> reloaded = daoImpl.findUndeterminedByMinimumAutoIdAndMoment(2010, 5, 0, 10);
		assertTrue(reloaded.get(0).getAutoId() < reloaded.get(1).getAutoId());
		assertTrue(reloaded.get(1).getAutoId() < reloaded.get(2).getAutoId());
	}
}
