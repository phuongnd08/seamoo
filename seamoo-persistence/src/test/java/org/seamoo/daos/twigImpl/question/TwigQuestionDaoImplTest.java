package org.seamoo.daos.twigImpl.question;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.seamoo.daos.ofyImpl.OfyModelRegistrar;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TwigQuestionDaoImplTest extends LocalAppEngineTest {
	QuestionDao daoImpl;

	@BeforeMethod
	@Override
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		daoImpl = new TwigQuestionDaoImpl();
	}

	@AfterMethod
	@Override
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	Long[] originalQuestionId;

	/**
	 * A method that used to generate questions for the sake of test. The method
	 * include both singular persistence and bulk persistence to ensure that the question dao
	 * has maintained a consistent index for all questions
	 * @param leagueId
	 * @param number
	 * @param bulkNumber
	 */
	public void generateQuestions(Long leagueId, int number, int bulkNumber) {
		originalQuestionId = new Long[number + bulkNumber];
		for (int i = 0; i < number; i++) {
			Question q = new Question();
			q.setLeagueAutoId(leagueId);
			daoImpl.persist(q);
			originalQuestionId[i] = q.getAutoId();
		}
		Question[] qs = new Question[bulkNumber];
		for (int i = 0; i < bulkNumber; i++) {
			qs[i] = new Question();
			qs[i].setLeagueAutoId(leagueId);
		}
		daoImpl.persist(qs);
		for (int i = 0; i < bulkNumber; i++)
			originalQuestionId[number + i] = qs[i].getAutoId();

	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void getRandomExceedAvailableThrowExeption() {
		generateQuestions(1L, 5, 0);
		daoImpl.getRandomQuestions(1L, 6);
	}

	@Test
	public void getRandomEqualAvailableProduceSameSet() {
		generateQuestions(1L, 5, 0);
		List<Question> qs = daoImpl.getRandomQuestions(1L, 5);
		List<Long> qsId = new ArrayList<Long>();
		for (Question q : qs)
			qsId.add(q.getAutoId());
		assertEqualsNoOrder(qsId.toArray(), originalQuestionId);
	}

	@Test
	public void getRandomSmallThanAvailableProduceSubDistinctSet() {
		generateQuestions(1L, 2, 3);
		List<Question> qs = daoImpl.getRandomQuestions(1L, 4);
		TreeSet<Long> idSet = new TreeSet<Long>();
		for (Question q : qs)
			idSet.add(q.getAutoId());
		assertEquals(idSet.size(), 4);
	}

	@Test
	public void getRandomDoesNotReturnCrossLeagueQuestion() {
		generateQuestions(1L, 2, 3);
		generateQuestions(2L, 5, 0);
		List<Question> qs = daoImpl.getRandomQuestions(1L, 5);
		for (Question q : qs)
			assertEquals(q.getLeagueAutoId(), new Long(1L));
	}

	@Test
	public void persistMultipleChoicesQuestionShouldBeOK() {
		Question q = new Question();
		MultipleChoicesQuestionRevision r = new MultipleChoicesQuestionRevision();
		r.setContent("hello");
		r.addChoice(new QuestionChoice("xxx", true));
		q.addAndSetAsCurrentRevision(r);
		daoImpl.persist(q);
		Question qReloaded = daoImpl.findByKey(q.getAutoId());
		MultipleChoicesQuestionRevision rReloaded = (MultipleChoicesQuestionRevision) qReloaded.getCurrentRevision();
		assertEquals("hello", rReloaded.getContent());
		assertEquals(1, rReloaded.getChoices().size());
		assertEquals("xxx", rReloaded.getChoices().get(0).getContent());
		assertEquals(true, rReloaded.getChoices().get(0).isCorrect());
	}
}
