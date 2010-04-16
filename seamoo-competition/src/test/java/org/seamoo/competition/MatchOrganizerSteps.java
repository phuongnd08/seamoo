package org.seamoo.competition;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jbehave.scenario.annotations.Alias;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchCandidate;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.question.Question;
import org.seamoo.persistence.daos.MemberDao;
import org.seamoo.persistence.matching.daos.MatchDao;
import org.seamoo.persistence.question.daos.QuestionDao;
import org.seamoo.utils.converter.Converter;

public class MatchOrganizerSteps {
	MatchOrganizer organizer;
	List<Match> activeMatches;

	@Given("There is no active match")
	public void setUpNoActiveMatch() {
		activeMatches = new ArrayList<Match>();
	}

	MemberDao memberDao;
	List<Member> members;

	@Given("A list of $number users")
	public void initListOfUsers(int number) {
		members = new ArrayList<Member>();
		for (int i = 0; i < number; i++) {
			Member member = new Member();
			member.setAutoId(new Long(i + 1));
			member.setDisplayName("User #" + (i + 1));
			members.add(member);
		}
		memberDao = mock(MemberDao.class);
		when(memberDao.findByAutoId(anyLong())).thenAnswer(new Answer<Member>() {

			@Override
			public Member answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				Long autoId = (Long) invocation.getArguments()[0];
				return members.get((int) (autoId - 1));
			}
		});
	}

	QuestionDao questionDao;

	@Given("A Question Dao")
	public void setUpQuestionDao() {
		questionDao = mock(QuestionDao.class);
		when(questionDao.getRandomQuestions(20)).thenReturn(Arrays.asList(new Question[20]));
	}

	MatchDao matchDao;

	@Given("A Match Dao")
	public void setUpMatchDao() {
		matchDao = mock(MatchDao.class);
	}

	List<Match> bufferedMatches;
	int invocationTimes = 0;

	@Given("Match Are Observered with buffer of $buffered objects")
	public void setUpMatchObserver(int buffered) throws Exception {
		bufferedMatches = new ArrayList<Match>();
		for (int i = 0; i < buffered; i++) {
			Match m = new Match();
			m.setAutoId(new Long(i + 1));
			bufferedMatches.add(m);
		}
		PowerMockito.mockStatic(EntityFactory.class);
		when(EntityFactory.newMatch()).thenAnswer(new Answer<Match>() {

			@Override
			public Match answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return bufferedMatches.get(invocationTimes++);
			}
		});

	}

	@When("Organizer is created")
	public void initMatchOrganizer() {
		organizer = new MatchOrganizer();
		organizer.memberDao = memberDao;
		organizer.questionDao = questionDao;
		organizer.matchDao = matchDao;
	}

	@Then("All Active Matches Are Loaded")
	public void assertActiveMatchesAreLoaded() {
		verify(matchDao).getAllActiveMatches();
	}

	private Date getDateFromHMS(int hour, int minute, int second) {
		return new Date(2010, 1, 1, hour, minute, second);
	}

	@Given("Current Time is $hour:$minute:$second")
	public void mockCurrentTime(int hour, int minute, int second) {
		Date d = getDateFromHMS(hour, minute, second);
		when(TimeStampProvider.getCurrentTimeMilliseconds()).thenReturn(d.getTime());
	}

	Match match;

	private int positionToNumber(String position) {
		return Converter.toInt(position.charAt(0));
	}

	@When("$position user request for Match")
	public void userRequestForMatch(String position) {
		int pos = positionToNumber(position) - 1;
		match = organizer.getMatchForUser(members.get(pos).getAutoId());
	}

	@Then("$position Match is created")
	public void assertMatchCreated(String position) {
		int times = positionToNumber(position);
		PowerMockito.verifyStatic(times(times));
		EntityFactory.newMatch();
	}

	@Then("$position Match Phase is $phase")
	public void assertMatchPhase(String position, String phase) {
		int pos = positionToNumber(position) - 1;
		MatchPhase p = MatchPhase.valueOf(phase);
		assertEquals(bufferedMatches.get(pos).getPhase(), p);
	}

	@Then("$position Match is returned")
	public void assertMatchReturning(String position) {
		int pos = positionToNumber(position) - 1;
		assertEquals(match, bufferedMatches.get(pos));

	}

	@Then("$position Match has $number players")
	@Alias("$position Match has $number player")
	public void assertNumberOfPlayersInMatch(String position, int number) {
		int pos = positionToNumber(position) - 1;
		assertEquals(bufferedMatches.get(pos).getCompetitors().size(), number);
	}

	@Given("$position user last signal is $hour:$minute:$second")
	public void mockUserLastSignal(String position, int hour, int minute, int second) {
		int pos = positionToNumber(position) - 1;
		MatchCandidate candidate = organizer.candidates.get(members.get(pos).getAutoId());
		candidate.setLastSeenMoment(getDateFromHMS(hour, minute, second).getTime());
	}

	@When("$position user submit $number answers")
	public void userSubmitAnswer(String position, int number) {
		int pos = positionToNumber(position) - 1;
		Member member = members.get(pos);
		for (int i = 0; i < number; i++) {
			organizer.submitAnswer(member.getAutoId(), i, "xx");
		}
	}

	@When("$position user ignore $number questions")
	public void userIgnoreQuestion(String position, int number) {
		int pos = positionToNumber(position) - 1;
		Member member = members.get(pos);
		for (int i = 0; i < number; i++) {
			organizer.ignoreQuestion(member.getAutoId(), i);
		}
	}

	@Then("Question Dao is requested for $number random questions")
	public void assertRandomQuestionsRequested(int number) {
		verify(questionDao, times(1)).getRandomQuestions(eq(number));
	}

	@Then("$position Match is assigned with $number questions")
	public void assertQuestionsAssignedToMatch(String position, int number) {
		int pos = positionToNumber(position) - 1;
		assertEquals(match.getQuestions().size(), number);
	}

	@Then("$position Match is persisted")
	public void assertMatchPersisted(String position) {
		int pos = positionToNumber(position) - 1;
		verify(matchDao).persist(bufferedMatches.get(pos));
	}

}
