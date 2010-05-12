package org.seamoo.daos.twigImpl.matching;

import java.util.Date;

import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchAnswer;
import org.seamoo.entities.matching.MatchAnswerType;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchEvent;
import org.seamoo.entities.matching.MatchEventType;
import org.seamoo.entities.question.Question;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.Assert;
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
		Assert.assertEquals(reloadedM.getCompetitors().get(0).getAnswers().size(), 2);
	}

	@Test
	public void competitorWithEmptyAnswersShouldBePersisted() {
		Match m = new Match();
		MatchCompetitor mc = new MatchCompetitor();
		m.addCompetitor(mc);
		daoImpl.persist(m);
		Assert.assertNotNull(m.getCompetitors().get(0).getAnswers());
	}
}
