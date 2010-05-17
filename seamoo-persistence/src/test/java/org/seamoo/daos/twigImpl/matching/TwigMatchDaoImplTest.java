package org.seamoo.daos.twigImpl.matching;

import static org.testng.Assert.*;

import java.util.Date;
import java.util.List;

import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchAnswer;
import org.seamoo.entities.matching.MatchAnswerType;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchEvent;
import org.seamoo.entities.matching.MatchEventType;
import org.seamoo.entities.question.Question;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

	@Test
	public void persistMatchWithQuestionAndEventAndCompetitorsAndAnswersShouldBeOK() {
		Match m = new Match();
		m.setQuestions(Lists.newArrayList(new Question[] { new Question() }));
		m.addEvent(new MatchEvent(MatchEventType.ANSWER_QUESTION, new Date(), new Member(), 1));
		MatchCompetitor competitor = new MatchCompetitor();
		competitor.addAnswer(new MatchAnswer(MatchAnswerType.SUBMITTED, "1"));
		competitor.addAnswer(new MatchAnswer(MatchAnswerType.IGNORED, "1"));
		m.addCompetitor(competitor);
		daoImpl.persist(m);
	}

	@Test
	public void matchCompetitorsAndAnswersShouldBePersisted() {
		Match m = new Match();
		m.setQuestions(Lists.newArrayList(new Question[] { new Question() }));
		m.addEvent(new MatchEvent(MatchEventType.ANSWER_QUESTION, new Date(), new Member(), 1));
		MatchCompetitor competitor = new MatchCompetitor();
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
		MatchCompetitor mc = new MatchCompetitor();
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
}
