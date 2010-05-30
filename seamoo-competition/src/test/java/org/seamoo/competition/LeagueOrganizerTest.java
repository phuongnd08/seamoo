package org.seamoo.competition;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.LeagueMembershipDao;
import org.seamoo.daos.MemberQualificationDao;
import org.seamoo.entities.League;
import org.seamoo.entities.LeagueMembership;
import org.seamoo.entities.LeagueResult;
import org.seamoo.entities.Member;
import org.seamoo.entities.MemberQualification;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.test.MockedTimeProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LeagueOrganizerTest {
	LeagueOrganizer leagueOrganizer;
	LeagueMembershipDao leagueMembershipDao;
	MemberQualificationDao memberQualificationDao;
	LeagueDao leagueDao;

	League zero = new League(), one = new League(), two = new League(), three = new League();

	@BeforeMethod
	public void setUp() {
		leagueOrganizer = new LeagueOrganizer();
		leagueMembershipDao = mock(LeagueMembershipDao.class);
		leagueOrganizer.leagueMembershipDao = leagueMembershipDao;
		memberQualificationDao = mock(MemberQualificationDao.class);
		leagueOrganizer.memberQualificationDao = memberQualificationDao;
		leagueDao = mock(LeagueDao.class);
		leagueOrganizer.leagueDao = leagueDao;

		zero.setLevel(0);
		zero.setSubjectAutoId(200L);
		one.setLevel(1);
		one.setSubjectAutoId(200L);
		two.setLevel(2);
		two.setSubjectAutoId(200L);
		three.setLevel(3);
		three.setSubjectAutoId(200L);
		when(leagueDao.findByKey(20L)).thenReturn(zero);
		when(leagueDao.findByKey(21L)).thenReturn(one);
		when(leagueDao.findByKey(22L)).thenReturn(two);
		when(leagueDao.findByKey(23L)).thenReturn(three);
	}

	@Test
	public void firstSeenUserCanJoinLevelZeroButOtherLeague() {
		assertTrue(leagueOrganizer.canMemberJoinLeague(null, zero));
		assertFalse(leagueOrganizer.canMemberJoinLeague(null, one));
		assertFalse(leagueOrganizer.canMemberJoinLeague(null, two));
		assertFalse(leagueOrganizer.canMemberJoinLeague(null, three));
	}

	@Test
	public void unqualifiedUserCanJoinLevelZeroButOtherLeaguse() {
		MemberQualification mq = new MemberQualification();
		mq.setLevel(0);
		assertTrue(leagueOrganizer.canMemberJoinLeague(mq, zero));
		assertFalse(leagueOrganizer.canMemberJoinLeague(mq, one));
		assertFalse(leagueOrganizer.canMemberJoinLeague(mq, two));
		assertFalse(leagueOrganizer.canMemberJoinLeague(mq, three));
	}

	@Test
	public void qualifiedForLevelTwoUserCanJoinLevelOneOrTwoButOtherLeagues() {
		MemberQualification mq = new MemberQualification();
		mq.setLevel(2);
		assertFalse(leagueOrganizer.canMemberJoinLeague(mq, zero));
		assertTrue(leagueOrganizer.canMemberJoinLeague(mq, one));
		assertTrue(leagueOrganizer.canMemberJoinLeague(mq, two));
		assertFalse(leagueOrganizer.canMemberJoinLeague(mq, three));
	}

	private MatchCompetitor getSampleCompetitor(long autoId, int rank) {
		MatchCompetitor competitor = new MatchCompetitor();
		competitor.setMemberAutoId(autoId);
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
	public void unqualifiedCompetitorWillAlsoBeUpdatedOnUpdatingUsingRankedMatch() {
		LeagueMembership lms = new LeagueMembership();
		lms.setAccumulatedScore(20);
		when(leagueMembershipDao.findByMemberAndLeagueAtCurrentMoment(1L, 20L)).thenReturn(lms);
		leagueOrganizer.timeProvider = new MockedTimeProvider(2010, 5);
		Match m = getSampleMatch(20L);
		m.getCompetitorForMember(1L).setTotalScore(13);
		leagueOrganizer.updateLeagueMembershipScore(m);
		verify(leagueMembershipDao).persist(lms);
	}

	private LeagueMembership getSampleLeagueMembership(Long memberAutoId, Long subjectAutoId, Long leagueAutoId,
			double accumulatedScore) {
		LeagueMembership lms = new LeagueMembership();
		// setup mallicious information: first seen member try to hack league 1
		lms.setAccumulatedScore(accumulatedScore);
		lms.setLeagueAutoId(leagueAutoId);
		lms.setMemberAutoId(memberAutoId);
		lms.setAutoId(memberAutoId + 10000);
		return lms;

	}

	private MemberQualification getSampleMemberQualification(Long memberAutoId, Long subjectAutoId, int level) {
		MemberQualification mq = new MemberQualification();
		mq.setMemberAutoId(memberAutoId);
		mq.setLevel(level);
		mq.setSubjectAutoId(subjectAutoId);
		return mq;
	}

	@Test
	public void hackedMemberWillNotBeRelegate() {
		LeagueMembership lms = new LeagueMembership();
		// setup mallicious information: first seen member try to hack league 1
		lms.setAccumulatedScore(350);
		lms.setLeagueAutoId(21L);
	}

	@Test
	public void recheckWillRelegateMemberAccordinglyToMembershipScore() {
		LeagueMembership lms1 = getSampleLeagueMembership(1L, 200L, 21L, 350);// uplevel
		LeagueMembership lms2 = getSampleLeagueMembership(2L, 200L, 22L, 50);// downlevel
		LeagueMembership lms3 = getSampleLeagueMembership(3L, 200L, 22L, 150);// stayed
		when(leagueMembershipDao.findUndeterminedByMinimumAutoIdAndMoment(2010, 5, 0, 10)).thenReturn(
				Arrays.asList(lms1, lms2, lms3));

		MemberQualification mq1 = getSampleMemberQualification(1L, 200L, 1);
		MemberQualification mq2 = getSampleMemberQualification(2L, 200L, 2);
		MemberQualification mq3 = getSampleMemberQualification(3L, 200L, 2);
		when(memberQualificationDao.findByMemberAndSubject(1L, 200L)).thenReturn(mq1);
		when(memberQualificationDao.findByMemberAndSubject(2L, 200L)).thenReturn(mq2);
		when(memberQualificationDao.findByMemberAndSubject(3L, 200L)).thenReturn(mq3);
		leagueOrganizer.timeProvider = new MockedTimeProvider(2010, 6);
		leagueOrganizer.recheckLeagueMembership(0, 10000);
		verify(memberQualificationDao).persist(mq1);
		verify(memberQualificationDao).persist(mq2);
		verify(memberQualificationDao).persist(mq3);
		verify(leagueMembershipDao).persist(lms1);
		verify(leagueMembershipDao).persist(lms2);
		verify(leagueMembershipDao).persist(lms3);
		assertEquals(mq1.getLevel(), 2);
		assertTrue(lms1.isResultCalculated());
		assertEquals(lms1.getResult(), LeagueResult.UP_RELEGATED);
		assertEquals(mq2.getLevel(), 1);
		assertTrue(lms2.isResultCalculated());
		assertEquals(lms2.getResult(), LeagueResult.DOWN_RELEGATED);
		assertEquals(mq3.getLevel(), 2);
		assertTrue(lms3.isResultCalculated());
		assertEquals(lms3.getResult(), LeagueResult.STAYED);
	}

	@Test
	public void recheckWillCreateQualificationForNewProvenMember() {
		LeagueMembership lms1 = getSampleLeagueMembership(1L, 200L, 20L, 350);// uplevel
		when(leagueMembershipDao.findUndeterminedByMinimumAutoIdAndMoment(2010, 5, 0, 10)).thenReturn(Arrays.asList(lms1));

		when(memberQualificationDao.findByMemberAndSubject(1L, 200L)).thenReturn(null);
		leagueOrganizer.timeProvider = new MockedTimeProvider(2010, 6);
		leagueOrganizer.recheckLeagueMembership(0, 10000);
		verify(memberQualificationDao).persist(argThat(new ArgumentMatcher<MemberQualification>() {

			@Override
			public boolean matches(Object argument) {
				// TODO Auto-generated method stub
				MemberQualification mq = (MemberQualification) argument;
				boolean memberIdOk = mq.getMemberAutoId() == 1L;
				boolean subjectIdOk = mq.getSubjectAutoId() == 200L;
				boolean levelOk = mq.getLevel() == 1;
				return memberIdOk && subjectIdOk && levelOk;
			}
		}));
		verify(leagueMembershipDao).persist(lms1);
		assertEquals(lms1.getResult(), LeagueResult.UP_RELEGATED);
	}

	@Test
	public void recheckReturnLastRangeNumberOfItemsProcessedWithinTimeLimit() {
		final List<LeagueMembership> samples = new ArrayList<LeagueMembership>();
		for (int i = 1; i <= 10; i++) {
			LeagueMembership lms = getSampleLeagueMembership(new Long(i), 200L, 21L, 350);
			samples.add(lms);
		}
		when(leagueMembershipDao.findUndeterminedByMinimumAutoIdAndMoment(2010, 5, 0, 10)).thenAnswer(new Answer<List>() {

			@Override
			public List answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				Thread.sleep(2);
				return samples;
			}
		});

		leagueOrganizer.timeProvider = new MockedTimeProvider(2010, 6);
		long result = leagueOrganizer.recheckLeagueMembership(0, 1);
		assertEquals(result, 10);
	}

	@Test
	public void recheckContinueFromTheLastProcessedItemWithinTimeLimit() {
		final List<LeagueMembership> samples = new ArrayList<LeagueMembership>();
		for (int i = 11; i <= 20; i++) {
			LeagueMembership lms = getSampleLeagueMembership(new Long(i), 200L, 21L, 350);
			samples.add(lms);
		}
		when(leagueMembershipDao.findUndeterminedByMinimumAutoIdAndMoment(2010, 5, 10, 10)).thenAnswer(new Answer<List>() {

			@Override
			public List answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				Thread.sleep(2);
				return samples;
			}
		});

		leagueOrganizer.timeProvider = new MockedTimeProvider(2010, 6);
		long result = leagueOrganizer.recheckLeagueMembership(10, 1);
		assertEquals(result, 20);
	}

	@Test
	public void recheckReturnMinusOneIfAllRecordProcessed() {
		LeagueMembership lms1 = getSampleLeagueMembership(1L, 200L, 21L, 350);// uplevel
		when(leagueMembershipDao.findUndeterminedByMinimumAutoIdAndMoment(2010, 5, 0, 10)).thenReturn(Arrays.asList(lms1));

		when(memberQualificationDao.findByMemberAndSubject(1L, 200L)).thenReturn(null);
		leagueOrganizer.timeProvider = new MockedTimeProvider(2010, 6);
		long result = leagueOrganizer.recheckLeagueMembership(0, 10000);
		assertEquals(result, -1);
	}

	@Test
	public void recheckOnDoubleMembershipRelegateUserToHighestLevel() {
		LeagueMembership lms11 = getSampleLeagueMembership(1L, 200L, 22L, 350);// uplevel 2->3
		LeagueMembership lms12 = getSampleLeagueMembership(1L, 200L, 21L, 350);// uplevel 1->2

		LeagueMembership lms21 = getSampleLeagueMembership(1L, 200L, 20L, 0);// stay 0->0
		LeagueMembership lms22 = getSampleLeagueMembership(1L, 200L, 21L, 150);// stay 1->1

		when(leagueMembershipDao.findUndeterminedByMinimumAutoIdAndMoment(2010, 5, 0, 10)).thenReturn(
				Arrays.asList(lms11, lms12, lms21, lms22));

		MemberQualification mq1 = getSampleMemberQualification(1L, 200L, 2);
		MemberQualification mq2 = getSampleMemberQualification(2L, 200L, 1);
		when(memberQualificationDao.findByMemberAndSubject(1L, 200L)).thenReturn(mq1);
		when(memberQualificationDao.findByMemberAndSubject(2L, 200L)).thenReturn(mq2);

		leagueOrganizer.timeProvider = new MockedTimeProvider(2010, 6);
		leagueOrganizer.recheckLeagueMembership(0, 1000000);

		assertEquals(3, mq1.getLevel());
		assertEquals(1, mq2.getLevel());
	}

}
