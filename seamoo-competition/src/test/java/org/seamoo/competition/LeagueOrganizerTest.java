package org.seamoo.competition;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.mockito.ArgumentMatcher;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.LeagueMembershipDao;
import org.seamoo.daos.MemberQualificationDao;
import org.seamoo.entities.League;
import org.seamoo.entities.LeagueMembership;
import org.seamoo.entities.Member;
import org.seamoo.entities.MemberQualification;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.utils.TimeProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LeagueOrganizerTest {
	LeagueOrganizer leagueOrganizer;
	LeagueMembershipDao leagueMembershipDao;
	MemberQualificationDao memberQualificationDao;
	LeagueDao leagueDao;

	@BeforeMethod
	public void setUp() {
		leagueOrganizer = new LeagueOrganizer();
		leagueMembershipDao = mock(LeagueMembershipDao.class);
		leagueOrganizer.leagueMembershipDao = leagueMembershipDao;
		memberQualificationDao = mock(MemberQualificationDao.class);
		leagueOrganizer.memberQualificationDao = memberQualificationDao;
		leagueDao = mock(LeagueDao.class);
		leagueOrganizer.leagueDao = leagueDao;

		League zero = new League(), one = new League(), two = new League(), three = new League();
		zero.setLevel(0);
		one.setLevel(1);
		two.setLevel(2);
		three.setLevel(3);
		when(leagueDao.findByKey(20L)).thenReturn(zero);
		when(leagueDao.findByKey(21L)).thenReturn(one);
		when(leagueDao.findByKey(22L)).thenReturn(two);
		when(leagueDao.findByKey(23L)).thenReturn(three);
	}

	@Test
	public void firstSeenUserCanJoinLevelZeroButOtherLeague() {
		when(memberQualificationDao.findByMember(1L)).thenReturn(null);
		assertTrue(leagueOrganizer.canMemberJoinLeague(1L, 20L));
		assertFalse(leagueOrganizer.canMemberJoinLeague(1L, 21L));
		assertFalse(leagueOrganizer.canMemberJoinLeague(1L, 22L));
		assertFalse(leagueOrganizer.canMemberJoinLeague(1L, 23L));
	}

	@Test
	public void unqualifiedUserCanJoinLevelZeroButOtherLeaguse() {
		MemberQualification mq = new MemberQualification();
		mq.setLevel(0);
		when(memberQualificationDao.findByMember(1L)).thenReturn(mq);
		assertTrue(leagueOrganizer.canMemberJoinLeague(1L, 20L));
		assertFalse(leagueOrganizer.canMemberJoinLeague(1L, 21L));
		assertFalse(leagueOrganizer.canMemberJoinLeague(1L, 22L));
		assertFalse(leagueOrganizer.canMemberJoinLeague(1L, 23L));
	}

	@Test
	public void qualifiedForLevelTwoUserCanJoinLevelOneOrTwoButOtherLeagues() {
		MemberQualification mq = new MemberQualification();
		mq.setLevel(2);
		when(memberQualificationDao.findByMember(1L)).thenReturn(mq);
		assertFalse(leagueOrganizer.canMemberJoinLeague(1L, 20L));
		assertTrue(leagueOrganizer.canMemberJoinLeague(1L, 21L));
		assertTrue(leagueOrganizer.canMemberJoinLeague(1L, 22L));
		assertFalse(leagueOrganizer.canMemberJoinLeague(1L, 23L));
	}

	private MatchCompetitor getSampleCompetitor(long autoId, int rank) {
		Member member = new Member();
		member.setAutoId(autoId);
		MatchCompetitor competitor = new MatchCompetitor();
		competitor.setMember(member);
		competitor.setRank(rank);
		competitor.setTotalScore(20);
		return competitor;
	}

	private Match getSampleMatch(Long leagueAutoId) {
		Match m = new Match();
		for (int i = 1; i <= 4; i++)
			m.addCompetitor(getSampleCompetitor(i, i));
		m.setLeagueAutoId(leagueAutoId);
		return m;
	}

	static class MockedTimeProvider extends TimeProvider {
		int mockedYear, mockedMonth;

		public MockedTimeProvider(int mockedYear, int mockedMonth) {
			this.mockedYear = mockedYear;
			this.mockedMonth = mockedMonth;
		}

		@Override
		public int getCurrentYear() {
			return mockedYear;
		}

		@Override
		public int getCurrentMonth() {
			return mockedMonth;
		}
	}

	@Test
	public void untrackedMemberWillBeTrackedOnUpdatingUsingRankedMatch() {
		when(leagueMembershipDao.findByMemberAndLeagueAtCurrentMoment(1L, 20L)).thenReturn(null);
		leagueOrganizer.timeProvider = new MockedTimeProvider(2010, 5);
		leagueOrganizer.updateLeagueMembershipScore(getSampleMatch(20L));
		verify(leagueMembershipDao).persist(argThat(new ArgumentMatcher<LeagueMembership>() {

			@Override
			public boolean matches(Object argument) {
				LeagueMembership lm = (LeagueMembership) argument;
				boolean scoreOk = (lm.getAccumulatedScore() == 4.0);
				boolean idOk = lm.getMemberAutoId() == 1L;
				boolean leagueIdOk = lm.getLeagueAutoId() == 20L;
				boolean yearOk = (lm.getYear() == 2010);
				boolean monthOk = (lm.getMonth() == 5);
				return scoreOk && idOk && leagueIdOk && yearOk && monthOk;
			}
		}));
	}

	@Test
	public void trackedMemberWillBeUpdatedOnUpdatingUsingRankedMatch() {
		LeagueMembership lms = new LeagueMembership();
		lms.setAccumulatedScore(20);
		when(leagueMembershipDao.findByMemberAndLeagueAtCurrentMoment(1L, 20L)).thenReturn(lms);
		leagueOrganizer.timeProvider = new MockedTimeProvider(2010, 5);
		leagueOrganizer.updateLeagueMembershipScore(getSampleMatch(20L));
		assertEquals(lms.getAccumulatedScore(), 24.0);
		verify(leagueMembershipDao).persist(lms);

	}

	@Test
	public void unqualifiedCompetitorWillNotBeUpdatedOnUpdatingUsingRankedMatch() {
		LeagueMembership lms = new LeagueMembership();
		lms.setAccumulatedScore(20);
		when(leagueMembershipDao.findByMemberAndLeagueAtCurrentMoment(1L, 20L)).thenReturn(lms);
		leagueOrganizer.timeProvider = new MockedTimeProvider(2010, 5);
		Match m = getSampleMatch(20L);
		m.getCompetitorForMember(1L).setTotalScore(13);
		leagueOrganizer.updateLeagueMembershipScore(m);
		verify(leagueMembershipDao, never()).persist(lms);
	}
}
