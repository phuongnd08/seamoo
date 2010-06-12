package org.seamoo.daos.twigImpl.matching;

import static org.testng.Assert.*;

import java.util.List;

import org.seamoo.daos.twigImpl.ObjectDatastoreProvider;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchAnswer;
import org.seamoo.entities.matching.MatchAnswerType;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.Delegate;
import com.google.inject.internal.Lists;

public class TwigMatchDaoImplTest extends LocalAppEngineTest {
	TwigMatchDaoImpl daoImpl;

	public TwigMatchDaoImplTest() {
	}

	@BeforeMethod
	@Override
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		daoImpl = new TwigMatchDaoImpl();
	}

	@AfterMethod
	@Override
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	private MatchCompetitor competitorFromMemberAutoId(Long autoId) {
		MatchCompetitor mc = new MatchCompetitor();
		mc.setMemberAutoId(autoId);
		return mc;
	}

	@Test
	public void persistMatchWithQuestionIdsAndCompetitorsAndAnswersShouldBeOK() {
		Match m = new Match();
		m.setQuestionIds(Lists.newArrayList(new Long[] { 1L }));
		MatchCompetitor competitor = competitorFromMemberAutoId(1L);
		competitor.addAnswer(new MatchAnswer(MatchAnswerType.SUBMITTED, "1"));
		competitor.addAnswer(new MatchAnswer(MatchAnswerType.IGNORED, "1"));
		m.addCompetitor(competitor);
		daoImpl.persist(m);
	}

	@Test
	public void matchCompetitorsAndAnswersShouldBePersisted() {
		Match m = new Match();
		m.setQuestionIds(Lists.newArrayList(new Long[] { 1L }));
		MatchCompetitor competitor = competitorFromMemberAutoId(1L);
		competitor.addAnswer(new MatchAnswer(MatchAnswerType.SUBMITTED, "1"));
		competitor.addAnswer(new MatchAnswer(MatchAnswerType.IGNORED, "1"));
		m.addCompetitor(competitor);
		daoImpl.persist(m);
		Match reloadedM = daoImpl.findByKey(m.getAutoId());
		assertEquals(reloadedM.getCompetitors().get(0).getAnswers().size(), 2);
	}

	// @Test
	public void competitorWithEmptyAnswersShouldBePersisted() {
		Match m = new Match();
		MatchCompetitor mc = competitorFromMemberAutoId(1L);
		m.addCompetitor(mc);
		daoImpl.persist(m);
		assertNotNull(m.getCompetitors().get(0).getAnswers());
	}

	@Test
	public void getRecentMatchesShouldReturnSegmentedResultInRevertChronologicalOrder() {
		Match[] ms = new Match[] { new Match(), new Match(), new Match(), new Match() };
		ms[0].setEndedMoment(10);
		ms[0].setLeagueAutoId(1L);
		ms[1].setEndedMoment(50);
		ms[1].setLeagueAutoId(1L);
		ms[2].setEndedMoment(100);
		ms[2].setLeagueAutoId(2L);
		daoImpl.persist(ms);
		List<Match> reloadedMs = daoImpl.getRecentMatchesByLeague(1L, 0, 2);
		assertEquals(reloadedMs.size(), 2);
		assertEquals(reloadedMs.get(0).getEndedMoment(), 50);
		assertEquals(reloadedMs.get(1).getEndedMoment(), 10);
	}

	@Test
	public void countByLeagueShouldReturnNumberOfMatchesWithinLeague() {
		Match[] ms = new Match[] { new Match(), new Match(), new Match(), new Match() };
		ms[0].setEndedMoment(10);
		ms[0].setLeagueAutoId(1L);
		ms[1].setEndedMoment(50);
		ms[1].setLeagueAutoId(1L);
		ms[2].setEndedMoment(100);
		ms[2].setLeagueAutoId(2L);
		daoImpl.persist(ms);
		assertEquals(daoImpl.countByLeague(2L), 1);
		assertEquals(daoImpl.countByLeague(1L), 2);
	}

	@Test
	public void countByMemberShouldRetunrNumberOfMatchesContainsMember() {
		Match[] ms = new Match[] { new Match(), new Match(), new Match(), new Match() };
		MatchCompetitor c1 = competitorFromMemberAutoId(1L);
		MatchCompetitor c2 = competitorFromMemberAutoId(2L);
		ms[0].addCompetitor(c1);
		ms[0].addCompetitor(c2);

		ms[1].addCompetitor(c1);

		ms[2].addCompetitor(c2);

		daoImpl.persist(ms);
		assertEquals(daoImpl.countByMember(1L), 2);
		assertEquals(daoImpl.countByMember(2L), 2);
	}

	@Test
	public void getByMemberShouldReturnMatchesContainsMember() {
		Match[] ms = new Match[] { new Match(), new Match(), new Match(), new Match() };
		MatchCompetitor c1 = competitorFromMemberAutoId(1L);
		MatchCompetitor c2 = competitorFromMemberAutoId(2L);
		ms[0].addCompetitor(c1);
		ms[0].setEndedMoment(0);
		ms[0].addCompetitor(c2);

		ms[1].addCompetitor(c1);
		ms[1].setEndedMoment(10);

		ms[2].addCompetitor(c2);
		ms[2].setEndedMoment(20);

		ms[3].addCompetitor(c1);
		ms[3].setEndedMoment(30);

		daoImpl.persist(ms);

		List<Match> reloaded = daoImpl.getRecentMatchesBymember(1L, 0, 2);
		assertEquals(reloaded.size(), 2);
		assertEquals(reloaded.get(0).getAutoId(), ms[3].getAutoId());
		assertEquals(reloaded.get(1).getAutoId(), ms[1].getAutoId());
	}

	@Test
	public void matchAnswersLoadedUsingSingleFind() {
		Match m = new Match();
		MatchCompetitor c = new MatchCompetitor();
		c.addAnswer(new MatchAnswer(MatchAnswerType.SUBMITTED, "xxx"));
		m.addCompetitor(c);
		daoImpl.persist(m);

		TwigMatchDaoImpl newDaoImpl = new TwigMatchDaoImpl();
		ReflectionTestUtils.setField(newDaoImpl, "objectDatastoreProvider", new ObjectDatastoreProvider(), null);
		Match reloadedMatch = newDaoImpl.findByKey(m.getAutoId());
		MatchCompetitor reloadedCompetitor = reloadedMatch.getCompetitors().get(0);
		assertEquals(reloadedCompetitor.getAnswers().size(), 1);
		assertEquals(reloadedCompetitor.getAnswers().get(0).getContent(), "xxx");
	}

	@Test
	public void matchAnswersNotLoadedUsingBulkLoad() {
		Delegate backup = ApiProxy.getDelegate();
		Match m = new Match();
		MatchCompetitor c = new MatchCompetitor();
		c.addAnswer(new MatchAnswer(MatchAnswerType.SUBMITTED, "xxx"));
		m.addCompetitor(c);
		daoImpl.persist(m);

		ApiProxy.setDelegate(new LoggingApiProxyDelegate(ApiProxy.getDelegate())); 
		TwigMatchDaoImpl newDaoImpl = new TwigMatchDaoImpl();
		ReflectionTestUtils.setField(newDaoImpl, "objectDatastoreProvider", new ObjectDatastoreProvider(), null);
		Match reloadedMatch = newDaoImpl.getAll().get(0);
		MatchCompetitor reloadedCompetitor = reloadedMatch.getCompetitors().get(0);
		assertEquals(reloadedCompetitor.getAnswers().size(), 1);
		assertNull(reloadedCompetitor.getAnswers().get(0).getContent());
		ApiProxy.setDelegate(backup);
	}
}
