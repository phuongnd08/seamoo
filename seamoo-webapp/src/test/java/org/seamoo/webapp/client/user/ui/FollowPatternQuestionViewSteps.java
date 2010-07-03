package org.seamoo.webapp.client.user.ui;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.entities.question.FollowPatternQuestionRevision;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.utils.converter.Converter;
import org.seamoo.webapp.client.shared.ui.UiObjectFactory;
import org.seamoo.webapp.client.uimocker.GwtUiMocker;
import org.seamoo.webapp.client.uimocker.MockedChangeable;
import org.seamoo.webapp.client.uimocker.MockedClickable;
import org.seamoo.webapp.client.user.ui.FollowPatternQuestionView.FollowPatternQuestionViewUiBinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class FollowPatternQuestionViewSteps {
	List<String> tableGuidingPatternText;
	FollowPatternQuestionView view;
	QuestionRevisionView.Listener listener;

	@Given("A Follow Pattern Question View")
	public void setUpView() {
		PowerMockito.mockStatic(GWT.class);
		when(GWT.create(FollowPatternQuestionViewUiBinder.class)).thenReturn(new FollowPatternQuestionViewUiBinder() {

			@Override
			public Widget createAndBindUi(FollowPatternQuestionView revisionView) {
				// TODO Auto-generated method stub
				return GwtUiMocker.mockUiField(revisionView, Widget.class);
			}
		});

		PowerMockito.mockStatic(UiObjectFactory.class);
		when(UiObjectFactory.newButton()).thenAnswer(new Answer<Button>() {

			@Override
			public Button answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return GwtUiMocker.getMockedWidget(Button.class);
			}
		});

		view = new FollowPatternQuestionView();
		tableGuidingPatternText = new ArrayList<String>();
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				int index = (Integer) (invocation.getArguments()[1]);
				if (index >= tableGuidingPatternText.size())
					for (int i = tableGuidingPatternText.size(); i <= index; i++) {
						tableGuidingPatternText.add("");
					}
				tableGuidingPatternText.set(index, (String) invocation.getArguments()[2]);
				return null;
			}
		}).when(view.tableGuidingPattern).setHTML(anyInt(), anyInt(), anyString());

		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				tableGuidingPatternText.clear();
				return null;
			}
		}).when(view.tableGuidingPattern).clear(true);

		listener = mock(QuestionRevisionView.Listener.class);

		view.getListenerMixin().add(listener);
	}

	FollowPatternQuestionRevision revision = null;

	@Given("A Follow Pattern Question Revision")
	public void createRevision() {
		revision = new FollowPatternQuestionRevision();
	}

	@Given("Revision Content is \"$content\"")
	public void setRevisionContent(String content) {
		revision.setContent(content);
	}

	@Given("Revision Guiding Pattern is $gPattern")
	public void setRevisionChoices(String gPattern) {
		revision.setGuidingPattern(gPattern);
	}

	@Then("$type span of text $chars are added to tableGuidingPattern")
	public void assertButtonAdded(String type, List<String> chars) {
		for (String c : chars) {
			String spanString = "<span class='guiding_" + type + "'>" + c + "</span>";
			assertEquals(tableGuidingPatternText.get(0), spanString);
			tableGuidingPatternText.remove(0);
		}
	}

	@Then("submit event is triggered with answer=$answer")
	public void assertSubmitEventTriggered(String answer) {
		verify(listener).submitAnswer(answer);
	}

	@When("Revision is bound to View")
	public void bindRevisionToView() {
		view.setQuestionRevision(revision);
	}

	public static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	private Widget getWidget(String widgetName) {
		try {
			Field field = null;
			field = FollowPatternQuestionView.class.getDeclaredField(widgetName);
			field.setAccessible(true);
			return (Widget) field.get(view);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Then("$property of $widget is \"$value\"")
	public void assertWidgetProperty(String property, String widget, String value) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Widget control = getWidget(widget);
		Method method = control.getClass().getMethod("get" + capitalize(property));
		Object actualValue = method.invoke(control);
		assertEquals(actualValue.toString(), value);
	}

	@When("answer is set to '$answer'")
	public void changeAnswer(String answer) {
		view.textboxAnswer.setText(answer);
		((MockedChangeable) view.textboxAnswer).change();
	}

	@When("User click submit button")
	public void submitAnswer() {
		((MockedClickable) view.buttonSubmitAnswer).click();
	}
}
