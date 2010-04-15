package org.seamoo.webapp.client;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jbehave.scenario.annotations.Alias;
import org.jbehave.scenario.annotations.Aliases;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.matching.MatchState;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.utils.converter.Converter;
import org.seamoo.webapp.client.MatchBoard.Display.EventListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MatchBoardSteps {

	Question[] questions;
	MatchBoard.Display.EventListener listener;
	int refreshPeriod;
	int periodLength;
	Timer oldTimer;
	Timer mockedTimer;

	@Given("Refresh interval is $refreshPeriod period of $periodLength milliseconds")
	public void setUpRefreshSettings(int refreshPeriod, int periodLength) {
		this.refreshPeriod = refreshPeriod;
		this.periodLength = periodLength;
	}

	MatchBoard.Display display;

	@Given("A Match Player Display")
	public void setUpMatchBoardDisplay() {
		display = mock(MatchBoard.Display.class);
		restubDisplay();
	}

	@Given("Display forgets its interaction")
	public void restubDisplay() {
		reset(display);
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				if (listener == null)
					listener = (EventListener) invocation.getArguments()[0];
				else
					throw new RuntimeException("listener assigned twice");
				return null;
			}
		}).when(display).addEventListener((EventListener) any());
	}

	@Given("$number questions to use in match")
	public void initQuestionsToUseInMatch(int number) {
		questions = new Question[number];
		for (int i = 0; i < number; i++) {
			questions[i] = new Question();
			questions[i].setCurrentRevision(new MultipleChoicesQuestionRevision());
		}
	}

	MatchServiceAsync serviceAsync;

	@Given("A Match Service Async")
	public void setUpMatchServiceAsync() {
		serviceAsync = mock(MatchServiceAsync.class);
		when(GWT.create(MatchService.class)).thenReturn(serviceAsync);
		restubService();
	}

	@Given("Service forgets its interaction")
	public void restubService() {
		reset(serviceAsync);
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				AsyncCallback<MatchState> callback = (AsyncCallback<MatchState>) invocation.getArguments()[1];
				callback.onSuccess(matchState);
				return null;
			}
		}).when(serviceAsync).getMatchState(anyInt(), (AsyncCallback<MatchState>) any());
	}

	AsyncCallback<MatchState> postPonedCallback;

	@Given("Service post-pone answer to query for match state")
	public void setUpServicePostPoneAnswer() {
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				postPonedCallback = (AsyncCallback<MatchState>) invocation.getArguments()[1];
				return null;
			}
		}).when(serviceAsync).getMatchState(anyInt(), (AsyncCallback<MatchState>) any());
	}

	@When("Answer to query for match state is returned")
	public void invokeServicePostPonedAnswer() {
		postPonedCallback.onSuccess(matchState);
	}

	MatchBoard.Presenter presenter;

	@Given("A Match Player Presenter")
	public void setUpMatchBoardPresenter() {
		presenter = new MatchBoard.Presenter();
		oldTimer = presenter.refreshTimer;
		mockedTimer = mock(Timer.class);
		presenter.refreshTimer = mockedTimer;
		restubTimer();
	}

	@Given("Timer forgets its interaction")
	public void restubTimer() {
		reset(mockedTimer);

		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				oldTimer.run();
				return null;
			}
		}).when(mockedTimer).run();

		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				oldTimer.cancel();
				return null;
			}
		}).when(mockedTimer).cancel();
	}

	MatchState matchState;

	@Given("Current match is empty")
	public void setupCurrentEmptyMatch() {
		matchState = new MatchState();
		matchState.setBufferedQuestions(new ArrayList<Question>());
		matchState.setBufferedFrom(0);
		matchState.setPhase(MatchPhase.NOT_FORMED);
	}

	@When("Presenter initialize")
	public void initializePresenter() {
		presenter.initialize(serviceAsync, display);
	}

	@Then("Service get the current match information with number of received question of $receivedQuestion")
	public void assertServiceGetCurrentMatchInformation(int receivedQuestion) {
		verify(serviceAsync).getMatchState(eq(receivedQuestion), (AsyncCallback<MatchState>) any());
	}

	@Then("Display is switched to $mode mode")
	public void assertDisplaySwitchedTo(String mode) {
		if (mode.equals("WAITING")) {
			verify(display).setPhase(MatchPhase.NOT_FORMED);
		} else if (mode.equals("PLAYING")) {
			verify(display).setPhase(MatchPhase.PLAYING);
		} else if (mode.equals("COUNTING-DOWN")) {
			verify(display).setPhase(MatchPhase.FORMED);
		} else if (mode.equals("FINISHED")) {
			verify(display).setPhase(MatchPhase.FINISHED);
		} else if (mode.equals("YOU_FINISHED")) {
			verify(display).setPhase(MatchPhase.YOU_FINISHED);
		} else
			throw new RuntimeException(String.format("mode '%s' is not supported", mode));
	}

	@Given("Current match is formed with count down of $countdownPeriod periods")
	public void setUpFormedMatch(int countdownPeriod) {
		matchState = new MatchState();
		matchState.setPhase(MatchPhase.FORMED);
		matchState.setBufferedFrom(0);
		matchState.setBufferedQuestions(new ArrayList<Question>());
		matchState.setRemainingPeriod(countdownPeriod);
	}

	@Given("Current match has $number players")
	public void setUpMatchBoards(int number) {
		matchState.setCompetitors(Arrays.asList(new MatchCompetitor[number]));
	}

	@When("Timer is fired on schedule")
	public void fireTimer() {
		presenter.refreshTimer.run();
	}

	@When("Display shows $number players")
	public void assertDisplayShowsPlayers(int number) {
		// verify(display).setCom
	}

	@Given("Current match is started with remaining periods of $remaining and number of questions of $questionCount")
	public void setUpStartedMatch(int remaining, int questionCount) {
		matchState = new MatchState();
		matchState.setPhase(MatchPhase.PLAYING);
		matchState.setPeriodLength(100);
		matchState.setRemainingPeriod(remaining);
		matchState.setQuestionsCount(questionCount);
	}

	@Given("$number questions from $index is associated to match")
	public void setUpBufferQuestions(int number, int index) {
		List<Question> subList = new ArrayList<Question>();
		for (int i = 0; i < number; i++)
			subList.add(questions[index + i]);
		matchState.setBufferedQuestions(subList);
		matchState.setBufferedFrom(index);
	}

	private int positionToNumber(String position) {
		return Converter.toInt(position.charAt(0));
	}

	@Then("Display is viewing $position question")
	public void assertDisplayViewQuestion(String position) {
		int pos = positionToNumber(position) - 1;
		verify(display).setQuestion(questions[pos]);
	}

	@When("User submit answer for current question")
	public void submitAnswer() {
		listener.submitAnswer(display, "xxx");
	}

	@Then("Service submit answer to server")
	public void assertServiceSubmitAnswer() {
		verify(serviceAsync).submitAnswer(anyInt(), anyString(), (AsyncCallback) any());
	}

	// !-- When user ignore the question
	@When("User ignore current question")
	public void ignoreQuestion() {
		listener.ignoreQuestion(display);
	}

	@When("User ignore $number more questions")
	@Aliases(values = { "User ignore $number more question", "User ignore $number questions" })
	public void ignoreMultipleQuestions(int number) {
		for (int i = 0; i < number; i++)
			listener.ignoreQuestion(display);
	}

	@Then("Service send ignore signal to server")
	public void assertServiceSendIgnoreSignal() {
		verify(serviceAsync).ignoreQuestion(anyInt(), (AsyncCallback) any());
	}

	@Given("Current match is finished")
	public void setUpFinishedMatch() {
		matchState = new MatchState();
		matchState.setMatchAutoId(1L);
		matchState.setPhase(MatchPhase.FINISHED);
	}

	@Then("Page is redirected to \"$url\"")
	public void assertPageRedirection(String url) {
		PowerMockito.verifyStatic();
		Window.Location.replace(url);
	}

	@Then("Timer is reset")
	public void assertTimerReset() {
		verify(mockedTimer).cancel();
	}

	@Then("Timer is rescheduled with interval of $interval")
	public void assertTimerRescheduled(int interval) {
		verify(mockedTimer).schedule(interval);
	}

	@Then("Service doesn't get the current match information")
	public void assertServiceDoesNotGetMatchState() {
		verify(serviceAsync, never()).getMatchState(anyInt(), (AsyncCallback<MatchState>) any());
	}

	@Then("Timer is not rescheduled")
	public void assertTimerIsNotRescheduled() {
		verify(mockedTimer, never()).schedule(anyInt());
	}

	@Given("Current match finished for current user")
	public void setUpMatchFinishedForCurrentUser() {
		matchState = new MatchState();
		matchState.setPhase(MatchPhase.YOU_FINISHED);
	}

	@Then("Page is not redirected")
	public void assertPageNotRedirect() {
		PowerMockito.verifyStatic(never());
		Window.Location.replace(anyString());
	}
}
