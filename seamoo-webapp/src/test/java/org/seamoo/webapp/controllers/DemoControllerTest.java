package org.seamoo.webapp.controllers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.installation.BundleDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.League;
import org.seamoo.installation.Bundle;
import org.seamoo.test.MockedTimeProvider;
import org.seamoo.utils.AbstractResourceIterator;
import org.seamoo.utils.ResourceIterator;
import org.seamoo.utils.ResourceIteratorProvider;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.repackaged.com.google.common.collect.Lists;

public class DemoControllerTest {
	DemoController controller;
	BundleDao bundleDao;
	ResourceIteratorProvider riProvider;
	LeagueDao leagueDao;
	MockedTimeProvider timeProvider;
	QuestionDao questionDao;

	@BeforeMethod
	public void setUp() {
		controller = new DemoController();
		bundleDao = mock(BundleDao.class);
		timeProvider = new MockedTimeProvider();
		riProvider = mock(ResourceIteratorProvider.class);
		leagueDao = mock(LeagueDao.class);
		questionDao = mock(QuestionDao.class);
		League league = new League();
		league.setAutoId(1L);
		when(leagueDao.findByKey(1L)).thenReturn(league);
		controller.resourceIteratorProvider = riProvider;
		controller.leagueDao = leagueDao;
		controller.bundleDao = bundleDao;
		controller.timeProvider = timeProvider;
		controller.questionDao = questionDao;
	}

	private ResourceIterator<String> getArrayIterator(String... items) {
		final List<String> list = Lists.newArrayList(items);
		return new AbstractResourceIterator<String>() {
			int index = 0;

			@Override
			public void remove() {
				// TODO Auto-generated method stub

			}

			@Override
			public String next() {
				String value = list.get(index);
				index++;
				return value;
			}

			@Override
			public boolean hasNext() {
				return index < list.size();
			}

			@Override
			public void close() {
				// TODO Auto-generated method stub

			}
		};
	}

	

	private void assertControllerOutcome(String bundleNameIn, Long finishedIn, Long leagueIdIn, String bundleNameOut,
			Long finishedOut, Long leagueIdOut) throws IOException {
		Queue queue = mock(Queue.class);
		final TaskOptions[] ts = new TaskOptions[1];
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				ts[0] = (TaskOptions) invocation.getArguments()[0];
				return null;
			}
		}).when(queue).add((TaskOptions) any());
		controller.internalInstallQuestions(bundleNameIn, finishedIn, leagueIdIn, queue);

		assertNotNull(ts[0]);
		assertEquals("/demo/install-questions", ts[0].getUrl());
		List params = getParams(ts[0]);
		int expectedParamSize = 0;
		if (bundleNameOut != null) {
			expectedParamSize++;
			assertParam(params.get(expectedParamSize - 1), "bundleName", bundleNameOut);
		}
		
		if (finishedOut != null){
			expectedParamSize++;
			assertParam(params.get(expectedParamSize-1), "finished", finishedOut.toString());
		}
		if (leagueIdOut!=null){
			expectedParamSize++;
			assertParam(params.get(expectedParamSize-1), "leagueId", leagueIdOut.toString());	
		}
		assertEquals(expectedParamSize, params.size());
	}

	List getParams(TaskOptions options) {
		return (List) ReflectionTestUtils.invokeGetterMethod(options, "params");
	}

	void assertParam(Object param, String name, String value) {
		assertEquals(ReflectionTestUtils.getField(param, "name"), name);
		assertEquals(ReflectionTestUtils.getField(param, "value"), value);
	}
	
	@Test
	public void unfinishedInstallShouldQueueNewTaskOnTheSameBundle() throws IOException {
		ResourceIterator<String> ri = mock(ResourceIterator.class);
		when(ri.iterator()).thenReturn(ri);
		when(ri.hasNext()).thenReturn(true);
		timeProvider.setCurrentTimeStamp(1);
		when(ri.next()).thenAnswer(new Answer<String>() {

			int index = -1;

			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				index++;
				if (index == DemoController.QUESTION_INSTALLATION_BATCH_SIZE - 1)
					timeProvider.setCurrentTimeStamp(1 + DemoController.MAX_DURATION + 1);
				return "q" + index + "|*a|b|c";
			}
		});
		when(riProvider.getIterator("resource_x")).thenReturn(ri);
		assertControllerOutcome("resource_x", 0L, 1L, "resource_x", DemoController.QUESTION_INSTALLATION_BATCH_SIZE, 1L);
	}

	@Test
	public void finishedInstallOneBundleShouldMarkBundleAsInstalledAndQueueCheckTask() throws IOException {
		ResourceIterator<String> ri = mock(ResourceIterator.class);
		when(ri.iterator()).thenReturn(ri);
		when(ri.hasNext()).thenAnswer(new Answer<Boolean>() {

			int index = -1;

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				index++;
				if (index < 2)
					return true;
				return false;
			}
		});
		timeProvider.setCurrentTimeStamp(1);
		when(ri.next()).thenAnswer(new Answer<String>() {

			int index = -1;

			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				index++;
				return "q" + index + "|*a|b|c";
			}
		});
		when(riProvider.getIterator("resource_x")).thenReturn(ri);
		assertControllerOutcome("resource_x", 0L, 1L, null, null, null);
		verify(bundleDao).persist(argThat(new ArgumentMatcher<Bundle>() {

			@Override
			public boolean matches(Object argument) {
				Bundle b = (Bundle) argument;
				return b.getName().equals("resource_x") && b.isInstalled();
			}
		}));

	}

	@Test
	public void checkShouldQueueInstallTaskOnFirstBundleIfNoBundleIsInstalled() throws IOException {
		when(bundleDao.getAll()).thenReturn(new ArrayList<Bundle>());
		when(riProvider.getIterator(DemoController.DATA_INDEX_PATH)).thenReturn(getArrayIterator("resource_x|1"));
		assertControllerOutcome(null, null, null, "resource_x", 0L, 1L);
	}


	@Test
	public void checkShouldQueueInstallTaskOnSecondBundleIfFirstBundleIsDone() throws IOException {
		when(riProvider.getIterator(DemoController.DATA_INDEX_PATH)).thenReturn(getArrayIterator("resource_x|1", "resource_y|1"));
		Bundle b = new Bundle();
		b.setFinished(100L);
		b.setName("resource_x");
		when(bundleDao.getAll()).thenReturn(Lists.newArrayList(b));
		assertControllerOutcome(null, null, null, "resource_y", 0L, 1L);
	}
}
