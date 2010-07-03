package org.seamoo.webapp.client.user.ui;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbehave.scenario.annotations.Alias;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchEvent;
import org.seamoo.entities.matching.MatchEventType;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.question.FollowPatternQuestionRevision;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.utils.converter.Converter;
import org.seamoo.webapp.client.shared.ListenerMixin.Caller;
import org.seamoo.webapp.client.shared.ui.UiObjectFactory;
import org.seamoo.webapp.client.uimocker.GwtUiMocker;
import org.seamoo.webapp.client.uimocker.MockedClickable;
import org.seamoo.webapp.client.user.MatchBoard;
import org.seamoo.webapp.client.user.MatchBoard.Display;
import org.seamoo.webapp.client.user.ui.MatchView.MatchViewUiBinder;
import org.seamoo.webapp.client.user.ui.QuestionRevisionView.Listener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

public class MatchViewSteps {
	MatchView matchView;
	MatchBoard.Display.EventListener listener;
	Question question;

	@Given("A Match Event Listener")
	public void initMatchEventListener() {
		listener = mock(MatchBoard.Display.EventListener.class);
	}

	List<Button> tableChoicesButtons;

	@Given("A Match View")
	public void initMatchView() {
		PowerMockito.mockStatic(GWT.class);
		when(GWT.create(MatchViewUiBinder.class)).thenReturn(new MatchViewUiBinder() {

			@Override
			public Widget createAndBindUi(MatchView matchView) {
				// TODO Auto-generated method stub
				return GwtUiMocker.mockUiField(matchView, Widget.class);
			}
		});

		PowerMockito.mockStatic(UiObjectFactory.class);

		when(UiObjectFactory.newCompetitorView()).thenAnswer(new Answer<CompetitorView>() {

			@Override
			public CompetitorView answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return GwtUiMocker.getMockedWidget(CompetitorView.class);
			}
		});
		matchView = new MatchView();
		matchView.getListenerMixin().add(listener);
		tableChoicesButtons = new ArrayList<Button>();
	}

	@When("View Players is assigned to $players")
	public void assignMatchCompetitors(List<String> players) {
		List<MatchCompetitor> competitors = new ArrayList<MatchCompetitor>();
		Map<Long, Member> membersMap = new HashMap<Long, Member>();
		Long memberId = 0L;
		for (String player : players) {
			memberId++;
			MatchCompetitor competitor = new MatchCompetitor();
			competitor.setMemberAutoId(memberId);
			membersMap.put(memberId, new Member());
			competitors.add(competitor);
		}
		matchView.setCompetitors(competitors, membersMap);
	}

	@When("View Phase is switched to $phase")
	public void switchMode(String phase) {
		matchView.setPhase(MatchPhase.valueOf(phase));
	}

	private Widget getWidget(String widgetName) {
		try {
			Field field = null;
			field = MatchView.class.getDeclaredField(widgetName);
			field.setAccessible(true);
			return (Widget) field.get(matchView);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Then("$widgets are visible")
	@Alias("$widgets is visible")
	public void assertFieldsShown(List<String> widgets) {
		assertFieldsVisibility(widgets, true);

	}

	@Then("$widgets are invisible")
	@Alias("$widgets is invisible")
	public void assertFieldsHidden(List<String> widgets) {
		assertFieldsVisibility(widgets, false);
	}

	public void assertFieldsVisibility(List<String> fields, boolean visible) {
		for (int i = 0; i < fields.size(); i++) {
			String field = fields.get(i);
			assertEquals(getWidget(field).isVisible(), visible, String.format("%s.visble <> %b", field, visible));
		}
	}

	@When("$widgetName is clicked")
	public void clickWidget(String widgetName) {
		Widget control = getWidget(widgetName);
		((MockedClickable) control).click();
	}

	@Then("$event event is triggered")
	public void assertEventTriggered(String event) {
		if (event.equalsIgnoreCase("ignore"))
			verify(listener).ignoreQuestion((Display) any());
		else if (event.equalsIgnoreCase("rematch"))
			verify(listener).rematch((Display) any());
		else if (event.equalsIgnoreCase("leaveMatch"))
			verify(listener).leaveMatch((Display) any());
		else
			throw new RuntimeException("Event " + event + " is not supported");
	}

	@Then("submit event is triggered with answer=$answer")
	public void assertSubmitEventTriggered(String answer) {
		verify(listener).submitAnswer((Display) any(), eq(answer));
	}

	public static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	@Then("$property of $widget is \"$value\"")
	public void assertWidgetProperty(String property, String widget, String value) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Widget control = getWidget(widget);
		Method method = control.getClass().getMethod("get" + capitalize(property));
		Object actualValue = method.invoke(control);
		assertEquals(actualValue.toString(), value);
	}

	@When("$property of $widget is changed to \"$value\"")
	public void changeWidgetProperty(String property, String widget, String value) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object v = value.equals("true") ? true : value.equals("false") ? false : value;
		Widget control = getWidget(widget);
		Class<?> paramClass = property.equals("value") ? Object.class : String.class;
		Method method = control.getClass().getMethod("set" + capitalize(property), paramClass);
		method.invoke(control, v);
	}

	@When("View Remaining Seconds is assigned to $seconds")
	public void setRemainingSeconds(int seconds) {
		matchView.setRemainingTime(seconds);
	}

	@When("View Question Index is assigned to $index")
	public void setQuestionIndex(int index) {
		matchView.setQuestionIndex(index);
	}

	@When("View Total Question is assigned to $total")
	public void setQuestionTotal(int total) {
		matchView.setTotalQuestion(total);
	}

	@When("View Question is assigned to $value")
	public void setQuestionToView(String alias) {
		Question q = null;
		if (alias.equals("Null"))
			q = null;
		else if (alias.equals("Multiple Choices Question"))
			q = getMultipleChoicesQuestion();
		else if (alias.equals("Follow Pattern Question"))
			q = getFollowPatternQuestion();
		else
			throw new RuntimeException("Do not support question=" + alias);
		matchView.setQuestion(q);
	}

	private Question getMultipleChoicesQuestion() {
		Question question = new Question();
		question.addAndSetAsCurrentRevision(new MultipleChoicesQuestionRevision());
		return question;
	}

	private Question getFollowPatternQuestion() {
		Question question = new Question();
		question.addAndSetAsCurrentRevision(new FollowPatternQuestionRevision());
		return question;
	}


	private int positionToNumber(String position) {
		return Converter.toInt(position.charAt(0));
	}

	private Member memberFromDisplayName(String displayName) {
		Member m = new Member();
		m.setDisplayName(displayName);
		m.setAlias(displayName);
		m.setAutoId(1L);
		return m;
	}

	List<MatchEvent> events = new ArrayList<MatchEvent>();

	@Given("Event#$index $displayName join Match")
	public void createJoinEvent(int index, String displayName) {
		MatchEvent e = new MatchEvent(MatchEventType.JOINED);
		e.setMember(memberFromDisplayName(displayName));
		events.add(e);
	}

	@Given("Event#$index $displayName submit answer for question #$questionIndex")
	public void createAnswerEvent(int index, String displayName, int questionIndex) {
		MatchEvent e = new MatchEvent(MatchEventType.ANSWER_QUESTION, new Date(), memberFromDisplayName(displayName),
				questionIndex);
		events.add(e);
	}

	@Given("Event#$index $displayName ignore question #$questionIndex")
	public void createIgnoreQuestionEvent(int index, String displayName, int questionIndex) {
		MatchEvent e = new MatchEvent(MatchEventType.IGNORE_QUESTION, new Date(), memberFromDisplayName(displayName),
				questionIndex);
		events.add(e);
	}

	@Given("Event#$index $displayName left Match")
	public void createLeftEvent(int index, String displayName) {
		MatchEvent e = new MatchEvent(MatchEventType.LEFT, new Date(), memberFromDisplayName(displayName));
		events.add(e);
	}

	@When("multiple choice view propagate answer=$answer")
	public void propagateAnswerFromMultipleChoiceView(final String answer) {
		matchView.multipleChoicesQuestionView.getListenerMixin().each(new Caller<Listener>() {
			@Override
			public void perform(Listener c) {
				c.submitAnswer(answer);
			}
		});
	}
	
	@When("follow pattern view propagate answer=$answer")
	public void propagateAnswerFromFollowPatternView(final String answer) {
		matchView.followPatternQuestionView.getListenerMixin().each(new Caller<Listener>() {
			@Override
			public void perform(Listener c) {
				c.submitAnswer(answer);
			}
		});
	}
}
