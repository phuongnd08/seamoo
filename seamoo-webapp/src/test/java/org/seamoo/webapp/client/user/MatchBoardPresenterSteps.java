package org.seamoo.webapp.client.user;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jbehave.scenario.annotations.Alias;
import org.jbehave.scenario.annotations.Aliases;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.entities.League;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.matching.MatchState;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.utils.converter.Converter;
import org.seamoo.webapp.client.shared.ListenerMixin;
import org.seamoo.webapp.client.shared.NotLoggedInException;
import org.seamoo.webapp.client.shared.ui.MessageBox;
import org.seamoo.webapp.client.user.MatchBoard.Display.EventListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;

public class MatchBoardPresenterSteps {

	Question[] questions;
	MatchBoard.Display.EventListener listener;
	public static final long TEST_LEAGUE_AUTOID = 1L;
	int refreshPeriod;
	Timer oldTimer;
	Timer mockedTimer;
	boolean notLoggedIn = false;

	@Given("Refresh period is $refreshPeriod milliseconds")
	public void setUpRefreshSettings(int refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}

	MatchBoard.Display display;

	@Given("A Match Player Display")
	public void setUpMatchBoardDisplay() {
		display = mock(MatchBoard.Display.class);
		restubDisplay();
	}

	Question currentDisplayedQuestion;

	@Given("Display forgets its interaction")
	public void restubDisplay() {
		reset(display);
		ListenerMixin mixin = mock(ListenerMixin.class);
		when(display.getListenerMixin()).thenReturn(mixin);
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
		}).when(mixin).add(any());

		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				currentDisplayedQuestion = (Question) invocation.getArguments()[0];
				return null;
			}
		}).when(display).setQuestion((Question) any());
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

	MessageBox.EventListener messageBoxListener;

	@Given("MessageBox is mocked")
	public void mockMessageBox() {
		PowerMockito.mockStatic(MessageBox.class);
		when(
				MessageBox.showPopupPanel(anyString(), (String[]) any(), (String[]) any(),
						(org.seamoo.webapp.client.shared.ui.MessageBox.EventListener) any())).thenAnswer(
				new Answer<PopupPanel>() {

					@Override
					public PopupPanel answer(InvocationOnMock invocation) throws Throwable {
						// TODO Auto-generated method stub
						messageBoxListener = (org.seamoo.webapp.client.shared.ui.MessageBox.EventListener) invocation.getArguments()[3];
						return null;
					}
				});
	}

	@When("\"$name\" is selected from DialogBox")
	public void fireSelectOKFromDialogBox(String name) {
		messageBoxListener.select(name);
	}

	@Given("Service forgets its interaction")
	public void restubService() {
		reset(serviceAsync);
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				AsyncCallback<MatchState> callback = (AsyncCallback<MatchState>) invocation.getArguments()[2];
				if (!notLoggedIn)
					callback.onSuccess(matchState);
				else
					callback.onFailure(new NotLoggedInException());
				return null;
			}
		}).when(serviceAsync).getMatchState(anyLong(), anyInt(), (AsyncCallback<MatchState>) any());

		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				AsyncCallback<League> callback = (AsyncCallback<League>) invocation.getArguments()[1];
				League league = new League();
				league.setAutoId(1L);
				league.setAlias("giai-ga-con");
				callback.onSuccess(league);
				return null;
			}
		}).when(serviceAsync).escapeCurrentMatch(anyLong(), (AsyncCallback<League>) any());

	}

	AsyncCallback<MatchState> postPonedCallback;

	@Given("Service post-pone answer to query for match state")
	public void setUpServicePostPoneAnswer() {
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				postPonedCallback = (AsyncCallback<MatchState>) invocation.getArguments()[2];
				return null;
			}
		}).when(serviceAsync).getMatchState(anyLong(), anyInt(), (AsyncCallback<MatchState>) any());
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
		matchState.setLeagueAutoId(TEST_LEAGUE_AUTOID);
		matchState.setBufferedQuestions(new ArrayList<Question>());
		matchState.setBufferedQuestionsFrom(0);
		matchState.setPhase(MatchPhase.NOT_FORMED);
	}

	@Given("Current user has answered $number questions")
	public void setUpCurrentUserCompletedQuestions(int number) {
		matchState.setCompletedQuestionsCount(number);
		matchState.setBufferedQuestions(Arrays.asList(questions));
	}

	@When("Presenter initialize")
	public void initializePresenter() {
		presenter.initialize(serviceAsync, display, TEST_LEAGUE_AUTOID);
	}

	@Then("Service get the current match information with receivedQuestion=$receivedQuestion")
	public void assertServiceGetCurrentMatchInformation(int receivedQuestion) {
		verify(serviceAsync).getMatchState(eq(1L), eq(receivedQuestion), (AsyncCallback<MatchState>) any());
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

	@Given("Current match is formed with count down of $countdownPeriod milliseconds")
	public void setUpFormedMatch(int countdownPeriod) {
		matchState = new MatchState();
		matchState.setPhase(MatchPhase.FORMED);
		matchState.setBufferedQuestionsFrom(0);
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
	public void assertDisplayShowsPlayers(final int number) {
		verify(display).setCompetitors(argThat(new ArgumentMatcher<List<MatchCompetitor>>() {

			@Override
			public boolean matches(Object argument) {
				// TODO Auto-generated method stub
				List<MatchCompetitor> competitors = (List<MatchCompetitor>) argument;
				return competitors.size() == number;
			}
		}), (Map<Long, Member>) any());
	}

	@Given("Current match is started with remaining periods of $remaining milliseconds and number of questions of $questionCount")
	public void setUpStartedMatch(int remaining, int questionCount) {
		matchState = new MatchState();
		matchState.setPhase(MatchPhase.PLAYING);
		matchState.setRemainingPeriod(remaining);
		matchState.setQuestionsCount(questionCount);
	}

	@Given("$number questions from $index is associated to match")
	public void setUpBufferQuestions(int number, int index) {
		List<Question> subList = new ArrayList<Question>();
		for (int i = 0; i < number; i++)
			subList.add(questions[index + i]);
		matchState.setBufferedQuestions(subList);
		matchState.setBufferedQuestionsFrom(index);
	}

	private int positionToNumber(String position) {
		return Converter.toInt(position.charAt(0));
	}

	@Then("Display is viewing $position question")
	public void assertDisplayViewQuestion(String position) {
		if (position.equals("no")) {
			assertNull(currentDisplayedQuestion);
		} else {
			int pos = positionToNumber(position) - 1;
			assertEquals(currentDisplayedQuestion, questions[pos]);
		}
	}

	@When("User submit answer for current question")
	public void submitAnswer() {
		listener.submitAnswer(display, "xxx");
	}

	@Then("Service submit answer of order $order to server")
	public void assertServiceSubmitAnswer(int order) {
		verify(serviceAsync).submitAnswer(eq(1L), eq(order), anyString(), (AsyncCallback) any());
	}

	@When("User ignore current question")
	public void ignoreQuestion() {
		listener.ignoreQuestion(display);
	}

	@When("Display invoke a leaveMatch")
	public void leaveMatch() {
		listener.leaveMatch(display);
	}

	@Then("a DialogBox is shown")
	public void assertDialogBoxShown() {
		PowerMockito.verifyStatic();
		MessageBox.showPopupPanel(anyString(), (String[]) any(), (String[]) any(),
				(org.seamoo.webapp.client.shared.ui.MessageBox.EventListener) any());
	}

	@When("User ignore $number more questions")
	@Aliases(values = { "User ignore $number more question", "User ignore $number questions" })
	public void ignoreMultipleQuestions(int number) {
		for (int i = 0; i < number; i++)
			listener.ignoreQuestion(display);
	}

	@Then("Service send ignore signal of order $order to server")
	public void assertServiceSendIgnoreSignal(int order) {
		verify(serviceAsync).ignoreQuestion(eq(1L), eq(order), (AsyncCallback) any());
	}

	@Given("Current match is finished")
	public void setUpFinishedMatch() {
		matchState = new MatchState();
		matchState.setMatchAutoId(1L);
		matchState.setMatchAlias("user1-user2");
		matchState.setPhase(MatchPhase.FINISHED);
	}
	
	@Given("Current match is finished with zero id")
	public void setUpFinishedZeroIdMatch(){
		matchState = new MatchState();
		matchState.setMatchAutoId(0L);
		matchState.setMatchAlias("user1-user2");
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
		verify(serviceAsync, never()).getMatchState(anyLong(), anyInt(), (AsyncCallback<MatchState>) any());
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

	@Then("Display remaining time is assigned to $remainingTime")
	public void assertRemainingTimeAssigned(int remainingTime) {
		verify(display).setRemainingTime(remainingTime);
	}

	@Then("Display question index is assigned to $questionIndex")
	public void assertQuestionIndexAssigned(int questionIndex) {
		verify(display).setQuestionIndex(questionIndex);
	}

	@Then("Display total question is assigned to $total")
	public void assertTotalQuestionAssigned(int total) {
		verify(display).setTotalQuestion(total);
	}

	@Then("Service get current match information $count times")
	@Alias("Service get current match information $count time")
	public void assertServiceGetMatchStateTimes(int count) {
		verify(serviceAsync, times(count)).getMatchState(anyLong(), anyInt(), (AsyncCallback<MatchState>) any());
	}

	@Given("User is not logged in")
	public void setUpUserNotLoggedIn() {
		notLoggedIn = true;
	}

	@Then("Page is reloaded")
	public void assertPageReloaded() {
		PowerMockito.verifyStatic();
		Window.Location.reload();
	}

	@Then("Service submit an escape")
	public void assertServiceSubmitEscape() {
		verify(serviceAsync).escapeCurrentMatch(eq(matchState.getLeagueAutoId()), (AsyncCallback) any());
	}

	@Then("Display is not viewing any question")
	public void assertDisplayNotViewingAnyQuestion() {
		verify(display).setQuestion(null);
	}
	
}
