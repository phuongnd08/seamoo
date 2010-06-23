package org.seamoo.webapp.controllers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.speed.NumericBagDao;
import org.seamoo.daos.speed.QuestionEventDao;
import org.seamoo.entities.League;
import org.seamoo.entities.question.Question;
import org.seamoo.lookup.NumericBag;
import org.seamoo.speed.QuestionEvent;
import org.seamoo.speed.QuestionEventType;
import org.seamoo.test.MockedTimeProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.repackaged.com.google.common.collect.Lists;

public class MaintainerControllerTest {
	MaintainerController controller;
	LeagueDao leagueDao;
	MockedTimeProvider timeProvider;
	QuestionEventDao questionEventDao;
	NumericBagDao numericBagDao;

	@BeforeMethod
	public void setUp() {
		controller = new MaintainerController();
		leagueDao = mock(LeagueDao.class);
		timeProvider = new MockedTimeProvider();
		questionEventDao = mock(QuestionEventDao.class);
		numericBagDao = mock(NumericBagDao.class);
		controller.leagueDao = leagueDao;
		controller.timeProvider = timeProvider;
		controller.questionEventDao = questionEventDao;
		controller.numericBagDao = numericBagDao;
	}

	@Test
	public void maintainQuestionIndexShouldUpdateQuestionShouldQueueUpdateForEveryLeagues() {
		FakeQueue q = new FakeQueue();
		League l1 = new League();
		l1.setAutoId(1L);
		League l2 = new League();
		l2.setAutoId(2L);
		List<League> leagues = Lists.newArrayList(l1, l2);
		when(leagueDao.getAll()).thenReturn(leagues);
		controller.internalRefreshQuestionIndexes(q);
		assertEquals(2, q.getTasks().size());
		TaskOptions task1 = q.getTasks().get(0);
		TaskOptions task2 = q.getTasks().get(1);
		assertEquals("/maintain/refresh-league-question-index", task1.getUrl());
		assertEquals("/maintain/refresh-league-question-index", task2.getUrl());
		List params1 = TaskQueueHelper.getParams(task1);
		assertEquals(1, params1.size());
		TaskQueueHelper.assertParam(params1.get(0), "leagueId", "1");
		List params2 = TaskQueueHelper.getParams(task2);
		assertEquals(1, params2.size());
		TaskQueueHelper.assertParam(params2.get(0), "leagueId", "2");

	}

	@Test
	public void maintainQuestionIndexForLeagueShouldPerformOperationWithinSafeTimeLimit() {
		when(questionEventDao.getByMinimumTimeStamp(eq(1L), anyLong(), eq(0), anyInt())).thenAnswer(new Answer<List>() {
			int times = 0;
			long questionAutoId = 0;

			@Override
			public List answer(InvocationOnMock invocation) throws Throwable {
				times++;
				if (times == 2) {
					timeProvider.setCurrentTimeStamp(1 + MaintainerController.MAX_QUESTION_INDEX_REFRESH_PERIOD);
				}
				int count = (Integer) invocation.getArguments()[3];
				List result = new ArrayList();
				questionAutoId++;
				for (int i = 0; i < count; i++)
					result.add(new QuestionEvent(QuestionEventType.CREATE, questionAutoId, 1L, 1));
				return result;
			}
		});
		timeProvider.setCurrentTimeStamp(1);
		controller.refreshLeagueQuestionIndexes(1L);
		verify(questionEventDao).getByMinimumTimeStamp(1L, 0, 0, 50);
		verify(questionEventDao).getByMinimumTimeStamp(1L, 1, 50, 50);
	}

	@Test
	public void maintainQuestionIndexForLeagueShouldStopAsSoonAsNumberOfQuestionEventRunOut() {
		when(questionEventDao.getByMinimumTimeStamp(eq(1L), anyLong(), anyInt(), anyInt())).thenAnswer(new Answer<List>() {
			int times = 0;
			long questionAutoId = 0;

			@Override
			public List answer(InvocationOnMock invocation) throws Throwable {
				times++;
				int count = (Integer) invocation.getArguments()[3];
				List result = new ArrayList();
				questionAutoId++;
				for (int i = 0; i < (times == 2 ? count - 1 : count); i++)
					result.add(new QuestionEvent(QuestionEventType.CREATE, questionAutoId, 1L, 1));
				return result;
			}
		});
		timeProvider.setCurrentTimeStamp(1);
		controller.refreshLeagueQuestionIndexes(1L);
		verify(questionEventDao).getByMinimumTimeStamp(1L, 0, 0, 50);
		verify(questionEventDao).getByMinimumTimeStamp(1L, 1, 50, 50);
	}

	@Test
	public void maintainQuestionIndexForLeagueChangeTheNumericBagAccordingly() {
		NumericBag bag = new NumericBag();
		for (int i = 0; i < 10; i++)
			bag.addNumber(new Long(i + 1));
		when(numericBagDao.findByClassifier(Question.class.getCanonicalName() + "@" + 1L)).thenReturn(bag);
		QuestionEvent e1 = new QuestionEvent(QuestionEventType.CREATE, 11L, 1L, 1);
		e1.setTimeStamp(100L);
		QuestionEvent e2 = new QuestionEvent(QuestionEventType.DELETE, 9L, 1L, 2);
		e2.setTimeStamp(120L);
		List events = Lists.newArrayList(e1, e2);
		when(questionEventDao.getByMinimumTimeStamp(eq(1L), anyLong(), eq(0), anyInt())).thenReturn(events);
		controller.refreshLeagueQuestionIndexes(1L);
		assertEquals(new Object[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 10L, 11L }, bag.toArray());
		verify(numericBagDao, times(1)).persist(bag);
		assertEquals(120L, bag.getLastUpdatedTimestamp());
		assertEquals(1, bag.getNumberOfRecentSameTimestamp());
	}

	@Test
	public void maintainQuestionIndexForLeageCreateNumericBagIfNotExist() {
		when(numericBagDao.findByClassifier(anyString())).thenReturn(null);
		QuestionEvent e1 = new QuestionEvent(QuestionEventType.CREATE, 11L, 1L, 1);
		QuestionEvent e2 = new QuestionEvent(QuestionEventType.DELETE, 9L, 1L, 2);
		List events = Lists.newArrayList(e1, e2);
		when(questionEventDao.getByMinimumTimeStamp(eq(1L), anyLong(), eq(0), anyInt())).thenReturn(events);
		controller.refreshLeagueQuestionIndexes(1L);
		verify(numericBagDao, times(1)).persist(argThat(new ArgumentMatcher<NumericBag>() {

			@Override
			public boolean matches(Object argument) {
				NumericBag bag = (NumericBag) argument;
				assertEquals(new Object[] { 11L }, bag.toArray());

				return bag.getClassifier().equals(Question.class.getCanonicalName() + "@" + 1L);
			}
		}));
	}
}
