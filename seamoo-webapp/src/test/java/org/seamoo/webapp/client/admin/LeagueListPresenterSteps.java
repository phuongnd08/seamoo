package org.seamoo.webapp.client.admin;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.jbehave.scenario.annotations.Alias;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.entities.League;
import org.seamoo.utils.converter.Converter;
import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueDisplay;
import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueListDisplay;
import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueDisplay.LeagueDisplayEventListener;
import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueDisplay.LeagueDisplayMode;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LeagueListPresenterSteps {

	LeagueListPresenter leagueListPresenter;
	LeagueListDisplay leagueListDisplay;
	LeagueDisplayEventListener[] leagueDisplayEventListeners;
	LeagueDisplay[] leagueDisplays;
	String[] leagueDisplayLogoUrls;
	LeagueDisplayMode[] leagueDisplayModes;
	League[] givenLeagues;
	League[] assignedLeagues;
	LeagueServiceAsync leagueServiceAsync;
	Element loadingMessage;
	Long currentSubjectId;

	@Given("Id of current Subject is $id")
	public void initCurrentSubjectId(long id) {
		currentSubjectId = id;
	}

	@Given("We have $number League")
	public void initLeagues(int number) {
		givenLeagues = new League[number];
		for (int i = 0; i < number; i++) {
			givenLeagues[i] = new League();
			givenLeagues[i].setName("" + i + "th");
		}
	}

	@Given("We have a LeagueServiceAsync")
	public void initLeagueServiceAsync() {
		leagueServiceAsync = mock(LeagueServiceAsync.class);
		// when get all leagues
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				AsyncCallback<List<League>> callback = (AsyncCallback<List<League>>) args[1];
				callback.onSuccess(Arrays.asList(givenLeagues));
				return null;
			}
		}).when(leagueServiceAsync).getAll(eq(currentSubjectId), (AsyncCallback<List<League>>) any());

		// when persist league
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				Object league = args[0];
				AsyncCallback<Object> callback = (AsyncCallback<Object>) args[1];
				callback.onSuccess(league);
				return null;
			}
		}).when(leagueServiceAsync).save(eq(currentSubjectId), (League) any(), (AsyncCallback<League>) any());

		// when delete league
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				AsyncCallback<Void> callback = (AsyncCallback<Void>) args[1];
				callback.onSuccess(null);
				return null;
			}
		}).when(leagueServiceAsync).delete((League) any(), (AsyncCallback<Void>) any());

	}

	@Given("We have $number LeagueDisplay")
	public void initLeagueDisplays(int number) {
		leagueDisplays = new LeagueDisplay[number];
		leagueDisplayLogoUrls = new String[number];
		leagueDisplayEventListeners = new LeagueDisplayEventListener[number];
		leagueDisplayModes = new LeagueDisplayMode[number];
		assignedLeagues = new League[number];
		for (int i = 0; i < number; i++) {
			leagueDisplays[i] = mock(LeagueDisplay.class);
			mockAddListenerAndSetter(leagueDisplays[i], i);
		}
	}

	@Given("We have a LeagueListPresenter")
	public void initLeagueListPresenter() {
		leagueListPresenter = new LeagueListPresenter();
	}

	private void mockAddListenerAndSetter(LeagueDisplay leagueDisplay, final int i) {
		// store event listener
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				leagueDisplayEventListeners[i] = (LeagueDisplayEventListener) invocation.getArguments()[0];
				return null;
			}
		}).when(leagueDisplay).addEventListener((LeagueDisplayEventListener) any());

		// response to setMode
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				leagueDisplayModes[i] = (LeagueDisplayMode) invocation.getArguments()[0];
				return null;
			}
		}).when(leagueDisplay).setMode((LeagueDisplayMode) any());

		// response to setLeague
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				assignedLeagues[i] = (League) invocation.getArguments()[0];
				return null;
			}
		}).when(leagueDisplay).setLeague((League) any());

		// response to setLogoUrl

		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				leagueDisplayLogoUrls[i] = (String) invocation.getArguments()[0];
				return null;
			}
		}).when(leagueDisplay).setLogoUrl(anyString());

	}

	int invokeTime = 0;

	@Given("We have a LeagueListDisplay")
	public void initLeagueListDisplay() {
		leagueListDisplay = mock(LeagueListDisplay.class);
		when(leagueListDisplay.createLeagueDisplay()).thenAnswer(new Answer<LeagueDisplay>() {

			@Override
			public LeagueDisplay answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				invokeTime++;
				return leagueDisplays[invokeTime - 1];
			}
		});
	}

	@When("LeagueListPresenter initialize")
	public void loadLeagueListPresenter() {
		leagueListPresenter.initialize(currentSubjectId, leagueListDisplay, leagueServiceAsync, null);
	}

	@Then("LeagueServiceAsync load all League of Subject $subjectId")
	public void assertLeagueServiceLoadAllMethod(long subjectId) {
		verify(leagueServiceAsync).getAll(eq(subjectId), (AsyncCallback<List<League>>) any());
	}

	@Then("LeagueServiceAsync $verb League")
	public void assertLeagueServiceAsyncUpdateWithCorrectSubjectId(String verb) {
		if (verb.equals("update")) {
			verify(leagueServiceAsync).save(eq(currentSubjectId), (League) any(), (AsyncCallback<League>) any());
		} else
			throw new RuntimeException(String.format("Verb %s is not supported", verb));
	}

	@Then("LeagueServiceAsync delete League $time time")
	public void assertLeagueServiceAsyncDeleteMethod(int time) {
		verify(leagueServiceAsync, times(time)).delete((League) any(), (AsyncCallback<Void>) any());
	}

	@Then("LeagueServiceAsync save League with subjectId equals $subjectId in $t times")
	public void assertLeagueServiceAsyncSaveWithCorrectSubjectId(long subjectId, int t) {
		verify(leagueServiceAsync, times(t)).save(eq(subjectId), (League) any(), (AsyncCallback<League>) any());
	}

	@Given("LeagueServiceAsync update will be successful")
	public void setupLeagueServiceAsyncOnSuccessHandler() {
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				AsyncCallback<League> callback = (AsyncCallback<League>) invocation.getArguments()[2];
				callback.onSuccess((League) invocation.getArguments()[1]);
				return null;
			}

		}).when(leagueServiceAsync).save(eq(currentSubjectId), (League) any(), (AsyncCallback<League>) any());
	}

	@Then("LeagueListDisplay $verb $number LeagueDisplay")
	public void assertLeagueListDisplayBehavior(String verb, int number) {
		if (verb.equals("create")) {
			verify(leagueListDisplay, times(number)).createLeagueDisplay();
		} else if (verb.equals("add")) {
			verify(leagueListDisplay, times(number)).addLeagueDisplay((LeagueDisplay) any());
		} else if (verb.equals("remove")) {
			verify(leagueListDisplay, times(number)).removeLeagueDisplay((LeagueDisplay) any());
		} else
			throw new RuntimeException(String.format("Verb %s (times: %s) is not supported", verb, number));
	}

	@Then("$position LeagueDisplay will be switched to $mode mode")
	public void assertLeagueDisplayMode(String position, String mode) {
		int pos = Converter.toInt(position.charAt(0)) - 1;// convert from
		// 1-based index to
		// 0-based index
		LeagueDisplayMode m = LeagueDisplayMode.valueOf(mode);
		assertEquals(leagueDisplayModes[pos], m);
	}

	@When("$position LeagueDisplay $event event is triggered")
	public void triggerLeagueDisplayEvent(String position, String event) {
		int pos = Converter.toInt(position.charAt(0)) - 1;// convert from
		// 1-based index to
		// 0-based index
		LeagueDisplay d = leagueDisplays[pos];
		LeagueDisplayEventListener l = leagueDisplayEventListeners[pos];
		League s = assignedLeagues[pos];
		if (event.equalsIgnoreCase("edit")) {
			l.edit(d);
		} else if (event.equals("create")) {
			l.create(d, new League());
		} else if (event.equals("update")) {
			l.update(d, s);
		} else if (event.equals("edit-cancel"))
			l.cancelEdit(d);
		else if (event.equals("delete"))
			l.delete(d, s);
		else if (event.equals("select-logo")) {
			l.selectLogo(d, null);
		} else
			throw new RuntimeException("Event not supported");
	}

	@Given("Confirm dialog will return $returnValue")
	public void setupConfirmDialog(String returnValue) {
		PowerMockito.mockStatic(Window.class);
		when(Window.confirm((String) any())).thenReturn(Converter.toBoolean(returnValue));
	}

	@Then("Confirm dialog will be shown")
	public void assertConfirmDialogShown() {
		// This is how to verify a call to static method the PowerMock way (how
		// lame it is)
		// http://code.google.com/p/powermock/wiki/MockitoUsage13
		PowerMockito.verifyStatic();
		Window.confirm((String) any());
	}

	@When("$position LeagueDisplay was assigned a League $t time")
	@Alias("$position LeagueDisplay was assigned a League $t times")
	public void assertNewLeagueAssignment(String position, int t) {
		int pos = Converter.toInt(position.charAt(0)) - 1;// convert from
		// 1-based index to
		// 0-based index
		verify(leagueDisplays[pos], times(t)).setLeague((League) any());
	}

	@Given("Input dialog will return \"$returnValue\"")
	public void setupInputDialog(String returnValue) {
		PowerMockito.mockStatic(Window.class);
		when(Window.prompt(anyString(), anyString())).thenReturn(returnValue.equals("null") ? null : returnValue);
	}

	@Then("Input dialog will be shown")
	public void assertInputDialogShown() {
		// This is how to verify a call to static method the PowerMock way (how
		// lame it is)
		// http://code.google.com/p/powermock/wiki/MockitoUsage13
		PowerMockito.verifyStatic();
		Window.prompt(anyString(), anyString());
	}

	@Then("logoUrl of $position LeagueDisplay is \"$url\"")
	public void assertLogoUrl(String position, String url) {
		int pos = Converter.toInt(position.charAt(0)) - 1;
		assertEquals(leagueDisplayLogoUrls[pos], url);
	}
}
