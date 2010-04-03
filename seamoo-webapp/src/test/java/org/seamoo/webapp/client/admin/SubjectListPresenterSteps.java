package org.seamoo.webapp.client.admin;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.jbehave.scenario.annotations.Alias;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.entities.Subject;
import org.seamoo.utils.converter.Converter;
import org.seamoo.webapp.client.admin.SubjectListPresenter.SubjectDisplay;
import org.seamoo.webapp.client.admin.SubjectListPresenter.SubjectListDisplay;
import org.seamoo.webapp.client.admin.SubjectListPresenter.SubjectDisplay.SubjectDisplayEventListener;
import org.seamoo.webapp.client.admin.SubjectListPresenter.SubjectDisplay.SubjectDisplayMode;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SubjectListPresenterSteps {

	SubjectListPresenter subjectListPresenter;
	SubjectListDisplay subjectListDisplay;
	SubjectDisplayEventListener[] subjectDisplayEventListeners;
	SubjectDisplay[] subjectDisplays;
	SubjectDisplayMode[] subjectDisplayModes;
	Subject[] givenSubjects;
	Subject[] assignedSubjects;
	SubjectServiceAsync subjectServiceAsync;
	Element loadingMessage;

	@Given("We have $number Subject")
	public void initSubjects(int number) {
		givenSubjects = new Subject[number];
		for (int i = 0; i < number; i++) {
			givenSubjects[i] = new Subject();
			givenSubjects[i].setName("" + i + "th");
		}
	}

	@Given("We have a SubjectServiceAsync")
	public void initSubjectServiceAsync() {
		subjectServiceAsync = mock(SubjectServiceAsync.class);
		// when get all subjects
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				AsyncCallback<List> callback = (AsyncCallback<List>) args[0];
				callback.onSuccess(Arrays.asList(givenSubjects));
				return null;
			}
		}).when(subjectServiceAsync).getAll((AsyncCallback<List>) any());

		// when persist subject
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				Object[] args = invocation.getArguments();
				Object subject = args[0];
				AsyncCallback<Object> callback = (AsyncCallback<Object>) args[1];
				callback.onSuccess(subject);
				return null;
			}
		}).when(subjectServiceAsync).persist(any(),
				(AsyncCallback<Object>) any());
	}

	@Given("We have $number SubjectDisplay")
	public void initSubjectDisplays(int number) {
		subjectDisplays = new SubjectDisplay[number];
		subjectDisplayEventListeners = new SubjectDisplayEventListener[number];
		subjectDisplayModes = new SubjectDisplayMode[number];
		assignedSubjects = new Subject[number];
		for (int i = 0; i < number; i++) {
			subjectDisplays[i] = mock(SubjectDisplay.class);
			mockAddListenerAndSetter(subjectDisplays[i], i);
		}
	}

	@Given("We have a SubjectListPresenter")
	public void initSubjectListPresenter() {
		subjectListPresenter = new SubjectListPresenter();
	}

	private void mockAddListenerAndSetter(SubjectDisplay subjectDisplay,
			final int i) {
		// store event listener
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				subjectDisplayEventListeners[i] = (SubjectDisplayEventListener) invocation
						.getArguments()[0];
				return null;
			}
		}).when(subjectDisplay).addEventListener(
				(SubjectDisplayEventListener) any());

		// response to setMode
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				subjectDisplayModes[i] = (SubjectDisplayMode) invocation
						.getArguments()[0];
				return null;
			}
		}).when(subjectDisplay).setMode((SubjectDisplayMode) any());

		// response to setSubject
		doAnswer(new Answer() {
			public Object answer(InvocationOnMock invocation) {
				assignedSubjects[i] = (Subject) invocation.getArguments()[0];
				return null;
			}
		}).when(subjectDisplay).setSubject((Subject) any());
	}

	@Given("We have a SubjectListDisplay")
	public void initSubjectListDisplay() {
		subjectListDisplay = mock(SubjectListDisplay.class);
		{
			OngoingStubbing<SubjectListPresenter.SubjectDisplay> mock = when(subjectListDisplay
					.createSubjectDisplay());
			for (int i = 0; i < subjectDisplays.length; i++)
				mock = mock.thenReturn(subjectDisplays[i]);
		}

	}

	@When("SubjectListPresenter initialize")
	public void loadSubjectListPresenter() {
		subjectListPresenter
				.initialize(subjectListDisplay, subjectServiceAsync);
	}

	@Then("SubjectServiceAsync $verb Subject")
	public void assertSubjectServiceAsyncMethod(String verb) {
		if (verb.equals("load all")) {
			verify(subjectServiceAsync).getAll((AsyncCallback<List>) any());
		}
	}

	@Then("SubjectListDisplay $verb $number SubjectDisplay")
	public void assertSubjectListDisplayBehavior(String verb, int number) {
		if (verb.equals("create")) {
			verify(subjectListDisplay, times(number)).createSubjectDisplay();
		} else if (verb.equals("add")) {
			verify(subjectListDisplay, times(number)).addSubjectDisplay(
					(SubjectDisplay) any());
		}
	}

	@Then("$position SubjectDisplay will be switched to $mode mode")
	public void assertSubjectDisplayMode(String position, String mode) {
		int pos = Converter.toInt(position.charAt(0)) - 1;// convert from
		// 1-based index to
		// 0-based index
		SubjectDisplayMode m = SubjectDisplayMode.valueOf(mode);
		assertEquals(subjectDisplayModes[pos], m);
	}

	@When("$position SubjectDisplay $event event is triggered")
	public void triggerSubjectDisplayEvent(String position, String event) {
		int pos = Converter.toInt(position.charAt(0)) - 1;// convert from
		// 1-based index to
		// 0-based index
		SubjectDisplay d = subjectDisplays[pos];
		SubjectDisplayEventListener l = subjectDisplayEventListeners[pos];
		Subject s = assignedSubjects[pos];
		if (event.equalsIgnoreCase("edit")) {
			l.edit(d);
		} else if (event.equals("create")) {
			l.create(d, new Subject());
		} else if (event.equals("update")) {
			l.update(d, s);
		} else if (event.equals("edit-cancel"))
			l.cancelEdit(d);
		else if (event.equals("delete"))
			l.delete(d, s);
		else
			throw new RuntimeException("Event not supported");
	}

	@Given("Confirm dialog will return $returnValue")
	public void setupConfirmDialog(String returnValue) {
		PowerMockito.mockStatic(Window.class);
		when(Window.confirm((String) any())).thenReturn(
				Converter.toBoolean(returnValue));
	}

	@Then("Confirm dialog will be shown")
	public void assertConfirmDialogShown() {
		// This is how to verify a call to static method the PowerMock way (how
		// lame it is)
		// http://code.google.com/p/powermock/wiki/MockitoUsage13
		PowerMockito.verifyStatic();
		Window.confirm((String) any());
	}

	@When("$position SubjectDisplay was assigned a Subject $t time")
	@Alias("$position SubjectDisplay was assigned a Subject $t times")
	public void assertNewSubjectAssignment(String position, int t) {
		int pos = Converter.toInt(position.charAt(0)) - 1;// convert from
		// 1-based index to
		// 0-based index
		verify(subjectDisplays[pos], times(t)).setSubject((Subject) any());
	}
}
