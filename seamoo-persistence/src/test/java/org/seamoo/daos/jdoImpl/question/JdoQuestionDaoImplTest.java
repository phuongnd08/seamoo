package org.seamoo.daos.jdoImpl.question;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.seamoo.entities.question.Question;
import org.seamoo.persistence.test.LocalDatastoreTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.apphosting.api.ApiProxy.ArgumentException;

public class JdoQuestionDaoImplTest extends LocalDatastoreTest {
	JdoQuestionDaoImpl daoImpl;

	@BeforeMethod
	@Override
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		daoImpl = new JdoQuestionDaoImpl();
	}

	@AfterMethod
	@Override
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	Long[] originalQuestionId;

	public void generateQuestions(int number, int bulkNumber) {
		originalQuestionId = new Long[number + bulkNumber];
		for (int i = 0; i < number; i++) {
			Question q = new Question();
			daoImpl.persist(q);
			originalQuestionId[i] = q.getAutoId();
		}
		Question[] qs = new Question[bulkNumber];
		for (int i = 0; i < bulkNumber; i++)
			qs[i] = new Question();
		daoImpl.persist(qs);
		for (int i = 0; i < bulkNumber; i++)
			originalQuestionId[number + i] = qs[i].getAutoId();

	}

	@Test(expectedExceptions = ArgumentException.class)
	public void getRandomExceedAvailableThrowExeption() {
		generateQuestions(5, 0);
		daoImpl.getRandomQuestions(6);
	}

	@Test
	public void getRandomEqualAvailableProduceSameSet() {
		generateQuestions(5, 0);
		List<Question> qs = daoImpl.getRandomQuestions(5);
		List<Long> qsId = new ArrayList<Long>();
		for (Question q : qs)
			qsId.add(q.getAutoId());
		assertEqualsNoOrder(qsId.toArray(), originalQuestionId);
	}

	@Test
	public void getRandomSmallThanAvailableProduceSubDistinctSet() {
		generateQuestions(2, 3);
		List<Question> qs = daoImpl.getRandomQuestions(4);
		TreeSet<Long> idSet = new TreeSet<Long>();
		for (Question q : qs)
			idSet.add(q.getAutoId());
		assertEquals(idSet.size(), 4);
	}
}
