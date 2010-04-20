package org.seamoo.webapp.client.user;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.entities.League;
import org.seamoo.entities.Subject;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionType;
import org.seamoo.utils.converter.Converter;
import org.seamoo.webapp.client.user.QuestionCreator;
import org.seamoo.webapp.client.user.UserQuestionService;
import org.seamoo.webapp.client.user.UserQuestionServiceAsync;
import org.seamoo.webapp.client.user.QuestionCreator.Display.EventListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class QuestionCreatorPresenterSteps {

	List<Subject> subjectsList;

	@Given("A list of subjects")
	public void initListOfSubjects() {
		subjectsList = Arrays.asList(new Subject[] { new Subject(), new Subject(), new Subject(), new Subject() });
		for (int i = 0; i <= 3; i++)
			subjectsList.get(i).setAutoId(new Long(i + 1));
	}

	List<League> leaguesList;

	@Given("A list of leagues")
	public void initListOfLeagues() {
		leaguesList = Arrays.asList(new League[] { new League(), new League(), new League(), new League() });
		for (int i = 0; i <= 3; i++)
			leaguesList.get(i).setAutoId(new Long(i + 1));
	}

	QuestionCreator.Display display;
	QuestionCreator.Display.EventListener listener;

	@Given("A QuestionCreatorDisplay")
	public void createDisplay() {
		display = mock(QuestionCreator.Display.class);
		restubDisplay();

	}

	@Given("QuestionCreatorDisplay forgot its interaction")
	public void restubDisplay() {
		reset(display);
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				if (listener == null)
					listener = (EventListener) invocation.getArguments()[0];
				else
					throw new RuntimeException("Listener registered twice for QuestionCreator.Display");
				return null;
			}
		}).when(display).addEventListener((EventListener) any());
	}

	QuestionCreator.Presenter presenter;

	@Given("A QuestionCreatorPresenter")
	public void createQuestionCreatorPresenter() {
		presenter = new QuestionCreator.Presenter();
	}

	UserQuestionServiceAsync serviceAsync;

	@Given("A UserQuestionServiceAsync")
	public void createUserQuestionServiceAsync() {
		serviceAsync = mock(UserQuestionServiceAsync.class);
		when(GWT.create(UserQuestionService.class)).thenReturn(serviceAsync);
		restubUserQuestionServiceAsync();
	}

	@Given("UserQuestionServiceAsync forgot its interaction")
	public void restubUserQuestionServiceAsync() {
		reset(serviceAsync);
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				AsyncCallback<List<Subject>> callback = (AsyncCallback<List<Subject>>) invocation.getArguments()[0];
				callback.onSuccess(subjectsList);
				return null;
			}

		}).when(serviceAsync).loadAllEnabledSubjects((AsyncCallback<List<Subject>>) any());

		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				AsyncCallback<List<League>> callback = (AsyncCallback<List<League>>) invocation.getArguments()[1];
				callback.onSuccess(leaguesList);
				return null;
			}

		}).when(serviceAsync).loadAllEnabledLeagues((Long) any(), (AsyncCallback<List<League>>) any());
	}

	@When("QuestionCreatorPresenter initialize")
	public void initializePresenter() {
		presenter.initialize(display);
	}

	@Then("UserQuestionServiceAsync load subjects list")
	public void assertServiceLoadSubjects() {
		verify(serviceAsync).loadAllEnabledSubjects((AsyncCallback<List<Subject>>) any());
	}

	private int positionToNumber(String position) {
		return Converter.toInt(position.charAt(0));
	}

	@Then("UserQuestionServiceAsync load leagues list of $position subject")
	public void assertServiceLoadLeagues(String position) {
		int pos = positionToNumber(position) - 1;
		verify(serviceAsync).loadAllEnabledLeagues(eq(subjectsList.get(pos).getAutoId()), (AsyncCallback<List<League>>) any());
	}

	@Then("QuestionCreatorDisplay is assigned a subjects list")
	public void assertSubjectsAssigned() {
		verify(display).setSubjects((List<Subject>) any());
	}

	@Then("QuestionCreatorDisplay is assigned a leagues list")
	public void assertLeaguesAssigned() {
		verify(display).setLeagues((List<League>) any());
	}

	@When("$position subject is selected")
	public void selectSubject(String position) {
		int pos = positionToNumber(position) - 1;
		listener.selectSubject(display, subjectsList.get(pos));
	}

	@Then("UserQuestionServiceAsync doesn't reload leagues list of 1st subject")
	public void assertSubjectsAreNotReloaded() {
		verify(serviceAsync, times(0)).loadAllEnabledLeagues(anyLong(), (AsyncCallback<List<League>>) any());
	}

	private QuestionType stringToQuestionType(String questionType) {
		QuestionType type;
		if (questionType.equals("multiple-choices"))
			type = QuestionType.MULTIPLE_CHOICES;
		else if (questionType.equals("fill-in-blank"))
			type = QuestionType.FILL_IN_BLANK;
		else if (questionType.equals("listening"))
			type = QuestionType.LISTENING;
		else
			throw new RuntimeException(String.format("QuestionType '%s' is not supported", questionType));
		return type;
	}

	@When("$questionType is selected as question type")
	public void selectQuestionType(String questionType) {
		QuestionType type = stringToQuestionType(questionType);
		listener.selectQuestionType(display, type);
	}

	@Then("QuestionCreatorDisplay question type is switched to $questionType")
	public void assertQuestionTypeSwitched(String questionType) {
		QuestionType type = stringToQuestionType(questionType);
		verify(display).setQuestionType(type);
	}

	@Given("QuestionCreatorDisplay return $position subject for getCurrentSubject")
	public void setUpDisplayCurrentSubject(String position) {
		int pos = positionToNumber(position) - 1;
		when(display.getCurrentSubject()).thenReturn(subjectsList.get(pos));
	}

	@Given("QuestionCreatorDisplay return $position league for getCurrentLeague")
	public void setUpDisplayCurrentLeague(String position) {
		int pos = positionToNumber(position) - 1;
		when(display.getCurrentLeague()).thenReturn(leaguesList.get(pos));
	}

	@Given("UserQuestionServiceAsync raise \"$msg\" on creating question")
	public void setUpExceptionOnAsyncSave(final String msg) {
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				AsyncCallback<Question> callback = (AsyncCallback<Question>) invocation.getArguments()[2];
				callback.onFailure(new RuntimeException(msg));
				return null;
			}
		}).when(serviceAsync).create(anyLong(), (Question) any(), (AsyncCallback<Question>) any());

	}

	@When("Question is saved")
	public void saveQuestion() {
		listener.saveQuestion(display, new Question());
	}

	@Then("UserQuestionServiceAsync save question with leagueId=$leagueId")
	public void assertServiceAsyncSaveQuestion(long leagueId) {
		verify(serviceAsync).create(eq(leagueId), (Question) any(), (AsyncCallback<Question>) any());
	}

	@Then("QuestionCreatorDisplay error message is assigned to \"$msg\"")
	public void assertErrorMessageSet(String msg) {
		verify(display).setErrorMessage(msg);
	}

	@Given("UserQuestionServiceAsync is successful on creating question with id=$id & alias=$alias")
	public void setUpServiceAsyncSaveQuestionSuccessfully(final Long id, final String alias) {
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				AsyncCallback<Question> callback = (AsyncCallback<Question>) invocation.getArguments()[2];
				Question q = new Question();
				q.setAutoId(id);
				q.setAlias(alias);
				callback.onSuccess(q);
				return null;
			}
		}).when(serviceAsync).create(anyLong(), (Question) any(), (AsyncCallback<Question>) any());
	}

	@Then("Page is redirected to \"$url\"")
	public void assertPageRedirection(String url) {
		PowerMockito.verifyStatic();
		Window.Location.replace(url);
	}

}
