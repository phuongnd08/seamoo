package org.seamoo.daos.twigImpl.question;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.daos.lookup.NumericBagDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.daos.speed.QuestionEventDao;
import org.seamoo.daos.twigImpl.ObjectDatastoreProvider;
import org.seamoo.daos.twigImpl.TwigGenericDaoImpl;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.lookup.NumericBag;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.seamoo.speed.QuestionEvent;
import org.seamoo.speed.QuestionEventType;
import org.seamoo.test.MockedTimeProvider;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

@PrepareForTest(ObjectDatastoreProvider.class)
public class TwigQuestionDaoImplTest extends LocalAppEngineTest {

	@ObjectFactory
	public IObjectFactory getObjectFactory() {
		return new org.powermock.modules.testng.PowerMockObjectFactory();
	}

	NumericBagDao numericBagDao;
	MockedTimeProvider timeProvider;
	QuestionEventDao questionEventDao;
	TwigQuestionDaoImpl twigQuestionDao;
	List<QuestionEvent> generatedQuestionEvents;

	@BeforeMethod
	@Override
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		twigQuestionDao = new TwigQuestionDaoImpl();
		timeProvider = new MockedTimeProvider();
		questionEventDao = mock(QuestionEventDao.class);
		numericBagDao = mock(NumericBagDao.class);

		generatedQuestionEvents = new ArrayList<QuestionEvent>();
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				generatedQuestionEvents.add((QuestionEvent) invocation.getArguments()[0]);
				return null;
			}
		}).when(questionEventDao).persist((QuestionEvent) any());

		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				QuestionEvent[] qes = (QuestionEvent[]) invocation.getArguments()[0];
				for (QuestionEvent qe : qes)
					generatedQuestionEvents.add(qe);
				return null;
			}
		}).when(questionEventDao).persist((QuestionEvent[]) any());
		ReflectionTestUtils.setField(twigQuestionDao, "objectDatastoreProvider", new ObjectDatastoreProvider());
		twigQuestionDao.timeProvider = timeProvider;
		twigQuestionDao.questionEventDao = questionEventDao;
		twigQuestionDao.numericBagDao = numericBagDao;
	}

	@AfterMethod
	@Override
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	Long[] originalQuestionId;

	/**
	 * A method that used to generate questions for the sake of test. The method include both singular persistence and bulk
	 * persistence to ensure that the question dao has maintained a consistent index for all questions
	 * 
	 * @param leagueId
	 * @param number
	 * @param bulkNumber
	 */
	public void generateQuestions(Long leagueId, int number) {
		originalQuestionId = new Long[number];
		NumericBag numericBag = new NumericBag();
		for (int i = 0; i < number; i++) {
			Question q = new Question();
			q.setLeagueAutoId(leagueId);
			twigQuestionDao.persist(q);
			originalQuestionId[i] = q.getAutoId();
			numericBag.addNumber(q.getAutoId());
		}
		when(numericBagDao.findByClassifier(NumericBag.getUniformClassifier(Question.class, leagueId))).thenReturn(numericBag);
	}

	@Test
	public void getRandomExceedAvailableThrowExeption() {
		generateQuestions(1L, 5);
		try {
			twigQuestionDao.getRandomQuestionKeys(1L, 6);
		} catch (IllegalArgumentException ex) {
			return;
		}
		fail("Expected IllegalArgumentException is not thrown");
	}

	@Test
	public void getRandomEqualAvailableProduceSameSet() {
		generateQuestions(1L, 5);
		List<Long> qsId = twigQuestionDao.getRandomQuestionKeys(1L, 5);
		assertEqualsNoOrder(qsId.toArray(), originalQuestionId);
	}

	@Test
	public void getRandomSmallThanAvailableProduceSubDistinctSet() {
		generateQuestions(1L, 5);
		List<Long> idList = twigQuestionDao.getRandomQuestionKeys(1L, 4);
		TreeSet<Long> idSet = new TreeSet<Long>();
		for (Long id : idList)
			idSet.add(id);
		assertEquals(idSet.size(), 4);
	}

	@Test
	public void getRandomDoesNotReturnCrossLeagueQuestion() {
		generateQuestions(1L, 5);
		generateQuestions(2L, 5);
		List<Long> idList = twigQuestionDao.getRandomQuestionKeys(1L, 5);
		for (Long id : idList)
			assertEquals(twigQuestionDao.findByKey(id).getLeagueAutoId(), new Long(1L));
	}

	@Test
	public void persistMultipleChoicesQuestionShouldBeOK() {
		Question q = new Question();
		MultipleChoicesQuestionRevision r = new MultipleChoicesQuestionRevision();
		r.setContent("hello");
		r.addChoice(new QuestionChoice("xxx", true));
		q.addAndSetAsCurrentRevision(r);
		twigQuestionDao.persist(q);
		Question qReloaded = twigQuestionDao.findByKey(q.getAutoId());
		MultipleChoicesQuestionRevision rReloaded = (MultipleChoicesQuestionRevision) qReloaded.getCurrentRevision();
		assertEquals("hello", rReloaded.getContent());
		assertEquals(1, rReloaded.getChoices().size());
		assertEquals("xxx", rReloaded.getChoices().get(0).getContent());
		assertEquals(true, rReloaded.getChoices().get(0).isCorrect());
	}

	@Test
	public void createNewQuestionShouldGenerateCreateQuestionEventOfCurrentTimeStamp() {
		timeProvider.setCurrentTimeStamp(100);
		Question q = new Question();
		twigQuestionDao.persist(q);
		assertEquals(generatedQuestionEvents.size(), 1);
		assertEquals(generatedQuestionEvents.get(0).getType(), QuestionEventType.CREATE);
		assertEquals(generatedQuestionEvents.get(0).getTimeStamp(), 100);
		assertEquals(generatedQuestionEvents.get(0).getQuestionAutoId().longValue(), 1);
	}

	@Test
	public void updateQuestionShouldNotGenerateCreateQuestionEvent() {
		Question q = new Question();
		twigQuestionDao.persist(q);
		MultipleChoicesQuestionRevision r = new MultipleChoicesQuestionRevision();
		q.addAndSetAsCurrentRevision(r);
		verify(questionEventDao, times(1)).persist((QuestionEvent) any());
	}

	@Test
	public void createMultipleQuestionsShouldGenerateMultipleQuestionEvents() {
		timeProvider.setCurrentTimeStamp(100);
		twigQuestionDao.persist(new Question[] { new Question(), new Question() });
		assertEquals(generatedQuestionEvents.size(), 2);
		for (int i = 0; i < 2; i++) {
			assertEquals(generatedQuestionEvents.get(i).getType(), QuestionEventType.CREATE);
			assertEquals(generatedQuestionEvents.get(i).getTimeStamp(), 100);
			assertEquals(generatedQuestionEvents.get(i).getQuestionAutoId().longValue(), i + 1);
		}

	}

	@Test
	public void deleteQuestionShouldGenerateCreateQuestionEventOfCurrentTimeStamp() {
		timeProvider.setCurrentTimeStamp(100);
		Question q = new Question();
		twigQuestionDao.persist(q);
		twigQuestionDao.delete(q);
		assertEquals(generatedQuestionEvents.size(), 2);
		assertEquals(generatedQuestionEvents.get(1).getType(), QuestionEventType.DELETE);
		assertEquals(generatedQuestionEvents.get(1).getTimeStamp(), 100);
		assertEquals(generatedQuestionEvents.get(1).getQuestionAutoId().longValue(), 1);
	}

	@Test
	public void questionIndexesRefreshedAfterOneMinute() {
		timeProvider.setCurrentTimeStamp(100);
		NumericBag n1 = new NumericBag();
		n1.addNumber(1L);
		NumericBag n2 = new NumericBag();
		n2.addNumber(2L);
		when(numericBagDao.findByClassifier(NumericBag.getUniformClassifier(Question.class, 1L))).thenReturn(n1);
		assertEquals(twigQuestionDao.getRandomQuestionKeys(1L, 1).toArray(), new Object[] { 1L });
		timeProvider.setCurrentTimeStamp(100 + 59000);
		when(numericBagDao.findByClassifier(NumericBag.getUniformClassifier(Question.class, 1L))).thenReturn(n2);
		assertEquals(twigQuestionDao.getRandomQuestionKeys(1L, 1).toArray(), new Object[] { 1L });
		timeProvider.setCurrentTimeStamp(100 + 60000);
		assertEquals(twigQuestionDao.getRandomQuestionKeys(1L, 1).toArray(), new Object[] { 2L });
	}

	private static class TwigMCQRDao extends TwigGenericDaoImpl<MultipleChoicesQuestionRevision, String> {

	}

	private static class TwigQuestionChoiceDao extends TwigGenericDaoImpl<QuestionChoice, Long> {

	}

	// @Test
	public void removeMultipleChoicesQuestionGetRidOfChildData() {
		Question q = new Question();
		MultipleChoicesQuestionRevision r = new MultipleChoicesQuestionRevision();
		r.setContent("hello");
		r.addChoice(new QuestionChoice("xxx", true));
		q.addAndSetAsCurrentRevision(r);
		twigQuestionDao.persist(q);
		twigQuestionDao.delete(q);
		TwigMCQRDao mcqrDao = new TwigMCQRDao();
		TwigQuestionChoiceDao qcDao = new TwigQuestionChoiceDao();
		assertEquals(mcqrDao.countAll(), 0);
		assertEquals(qcDao.countAll(), 0);
	}

}
