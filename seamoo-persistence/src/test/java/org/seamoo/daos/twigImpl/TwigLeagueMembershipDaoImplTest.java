package org.seamoo.daos.twigImpl;

import static org.testng.Assert.*;

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

	@Test
	public void getByMemberAndLeagueShouldReturnMatchingMembership() {
		LeagueMembership lms = new LeagueMembership();
		lms.setMemberAutoId(1L);
		lms.setLeagueAutoId(2L);
		lms.setYear(2010);
		lms.setMonth(5);
		TwigLeagueMembershipDaoImpl daoImpl = new TwigLeagueMembershipDaoImpl();
		daoImpl.persist(lms);
		LeagueMembership reloadedLms = daoImpl.findByMemberAndLeagueAtMoment(1L, 2L, 2010, 5);
		assertNotNull(reloadedLms);
	}

}
