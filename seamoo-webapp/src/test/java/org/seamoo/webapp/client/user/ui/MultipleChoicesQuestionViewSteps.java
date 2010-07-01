package org.seamoo.webapp.client.user.ui;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.utils.converter.Converter;
import org.seamoo.webapp.client.shared.ui.UiObjectFactory;
import org.seamoo.webapp.client.uimocker.GwtUiMocker;
import org.seamoo.webapp.client.uimocker.MockedClickable;
import org.seamoo.webapp.client.user.MatchBoard;
import org.seamoo.webapp.client.user.MatchBoard.Display;
import org.seamoo.webapp.client.user.ui.MultipleChoicesQuestionView.MultipleChoicesQuestionViewUiBinder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class MultipleChoicesQuestionViewSteps {
	List<Button> tableChoicesButtons;
	MultipleChoicesQuestionView view;
	QuestionRevisionView.Listener listener;

	@Given("A Mutiple Choice Question View")
	public void setUpView() {
		PowerMockito.mockStatic(GWT.class);
		when(GWT.create(MultipleChoicesQuestionViewUiBinder.class)).thenReturn(new MultipleChoicesQuestionViewUiBinder() {

			@Override
			public Widget createAndBindUi(MultipleChoicesQuestionView revisionView) {
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

		view = new MultipleChoicesQuestionView();
		tableChoicesButtons = new ArrayList<Button>();
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				tableChoicesButtons.add((Button) invocation.getArguments()[2]);
				return null;
			}
		}).when(view.tableChoices).setWidget(anyInt(), anyInt(), (Widget) any());

		listener = mock(QuestionRevisionView.Listener.class);

		view.getListenerMixin().add(listener);
	}

	MultipleChoicesQuestionRevision revision = null;

	@Given("A Multiple Choice Question Revision")
	public void createRevision() {
		revision = new MultipleChoicesQuestionRevision();
	}

	@Given("Revision Content is \"$content\"")
	public void setRevisionContent(String content) {
		revision.setContent(content);
	}

	@Given("Revision Choices are $choices")
	public void setRevisionChoices(List<String> choices) {
		for (String choice : choices) {
			revision.addChoice(new QuestionChoice(choice, false));
		}
	}

	private int positionToNumber(String position) {
		return Converter.toInt(position.charAt(0));
	}

	@When("User click $position button of panelChoices")
	public void clickChoiceButton(String position) {
		int pos = positionToNumber(position) - 1;
		((MockedClickable) tableChoicesButtons.get(pos)).click();
	}

	@Then("$number buttons of text $choices are added to panelChoices")
	public void assertButtonAdded(List<String> choices) {
		for (String choice : choices) {
			verify(view.tableChoices).setWidget(anyInt(), anyInt(), argThat(getButtonMatcher(choice)));
		}
	}

	@Then("submit event is triggered with answer=$answer")
	public void assertSubmitEventTriggered(String answer) {
		verify(listener).submitAnswer(answer);
	}

	private Matcher<Widget> getButtonMatcher(final String choice) {
		return new ArgumentMatcher<Widget>() {

			@Override
			public boolean matches(Object argument) {
				if (!(argument instanceof Button))
					return false;
				return ((Button) argument).getText().equals(choice);
			}
		};
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
			field = MultipleChoicesQuestionView.class.getDeclaredField(widgetName);
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
}
