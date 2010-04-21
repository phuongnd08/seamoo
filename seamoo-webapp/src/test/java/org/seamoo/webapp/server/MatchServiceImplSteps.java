package org.seamoo.webapp.server;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.competition.MatchOrganizer;
import org.seamoo.competition.TimeStampProvider;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchEvent;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.matching.MatchState;
import org.seamoo.entities.question.Question;
import org.workingonit.gwtbridge.ServletUtils;

public class MatchServiceImplSteps {
	MatchOrganizer mo;
	Match currentMatch;

	@Given("A MatchOrganizer")
	public void initMatchOrganizer() {
		mo = mock(MatchOrganizer.class);
		currentMatch = new Match();
		when(mo.getMatchForUser(anyLong())).thenReturn(currentMatch);
	}

	MatchServiceImpl service;

	@Given("A MatchServiceImpl")
	public void initMatchServiceImp() {
		service = new MatchServiceImpl();
		service.matchOrganizer = mo;
	}

	Map<Long, Member> members;

	@Given("There are $number Members $list")
	public void initMembers(int number, List<String> list) {
		members = new HashMap<Long, Member>();
		for (int i = 0; i < list.size(); i++) {
			Long memberId = Long.parseLong(list.get(i));
			Member m = new Member();
			m.setAutoId(memberId);
			members.put(memberId, m);
		}
	}

	Member currentMember;

	@Given("Current Member is Member@$id")
	public void setUpCurrentMember(Long id) {
		currentMember = members.get(id);
	}

	@Given("Match Phase is $phase")
	public void setUpMatchPhase(String phase) {
		currentMatch.setPhase(MatchPhase.valueOf(phase));
	}

	@Then("State Remaining Time is $time")
	public void assertMatchStateRemainingTime(long time) {
		assertEquals(matchState.getRemainingPeriod(), time);
	}

	@Given("Match has $number questions")
	public void setUpMatchQuestions(int number) {
		List<Question> qs = new ArrayList<Question>();
		for (int i = 0; i < number; i++)
			qs.add(new Question());
		currentMatch.setQuestions(qs);
	}

	@Given("Match has $number events")
	public void setUpMatchEvents(int number) {
		for (int i = currentMatch.getEvents().size(); i < number; i++) {
			currentMatch.addEvent(new MatchEvent());
		}
	}

	int bufferedQuestionsCount = 0, bufferedEventsCount = 0;
	MatchState matchState;

	@When("Member@$id request for Match State")
	public void requestMatchState(Long id) {
		setUpCurrentMember(members.get(id));
		matchState = service.getMatchState(bufferedQuestionsCount, bufferedEventsCount);
	}

	private void setUpCurrentMember(Member member) {
		// TODO Auto-generated method stub
		PowerMockito.mockStatic(ServletUtils.class);
		HttpServletRequest rq = mock(HttpServletRequest.class);
		when(rq.getAttribute("member")).thenReturn(member);
		when(ServletUtils.getRequest()).thenReturn(rq);
	}

	@Then("State Phase is $phase")
	public void assertMatchStatePhase(String phase) {
		assertEquals(matchState.getPhase(), MatchPhase.valueOf(phase));
	}

	@Given("Match have $number Members $list")
	public void setUpMatchCompetitors(int number, List<Long> list) {
		for (Long id : list) {
			MatchCompetitor competitor = new MatchCompetitor();
			competitor.setMember(members.get(id));
			currentMatch.addCompetitor(competitor);
		}
	}

	@Given("Match Formed Time is $time")
	public void setUpMatchFormedTime(long time) {
		currentMatch.setFormedMoment(time);
	}

	@Given("Match Started Time is $time")
	public void setUpMatchStartedTime(long time) {
		currentMatch.setStartedMoment(time);
	}

	@Given("Match Ended Time is $time")
	public void setUpMatchEndedTime(long time) {
		currentMatch.setEndedMoment(time);
	}

	@Given("Current Member has $count buffered questions")
	public void setUpCurrentMemberBufferedQuestions(int count) {
		bufferedQuestionsCount = count;
	}

	@Given("Current Member has $count buffered events")
	public void setUpCurrentMemberBufferedEvents(int count) {
		bufferedEventsCount = count;
	}

	@Given("Member@$id finished the match")
	public void setUpMemberFinishedMatch(Long id) {
		currentMatch.getCompetitorForMember(members.get(id)).setFinishedMoment(1L);
	}

	@Given("Current Time is $stamp")
	public void setUpCurrentTime(long stamp) {
		PowerMockito.mockStatic(TimeStampProvider.class);
		when(TimeStampProvider.getCurrentTimeMilliseconds()).thenReturn(stamp);
	}

	@Then("State Buffered Questions has $number questions")
	public void assertNumberOfBufferedQuestionsOfMatchState(int number) {
		assertEquals(matchState.getBufferedQuestions().size(), number);
	}

	@Then("State Buffered Events has $number events")
	public void assertNumberOfBufferedEventsOfMatchState(int number) {
		assertEquals(matchState.getBufferedEvents().size(), number);
	}

}
