package org.seamoo.webapp.server;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.competition.LeagueOrganizer;
import org.seamoo.competition.MatchOrganizer;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.matching.MatchState;
import org.seamoo.entities.question.Question;
import org.seamoo.test.MockedTimeProvider;
import org.seamoo.webapp.client.shared.NotLoggedInException;
import org.workingonit.gwtbridge.ServletUtils;

import com.google.common.collect.Lists;

public class MatchServiceImplSteps {
	LeagueOrganizer lo;
	MatchOrganizer mo;
	Match currentMatch;
	MemberDao memberDao;
	QuestionDao questionDao;
	MockedTimeProvider timeProvider;

	@Given("A MatchOrganizer")
	public void initMatchOrganizer() throws TimeoutException {
		mo = mock(MatchOrganizer.class);
		lo = mock(LeagueOrganizer.class);
		currentMatch = new Match();
		currentMatch.setLeagueAutoId(1L);
		when(mo.getMatchForUser(anyLong())).thenReturn(currentMatch);
		when(lo.getMatchOrganizer(1L)).thenReturn(mo);
	}

	@Given("A MemberDao")
	public void initMemberDao() {
		memberDao = mock(MemberDao.class);
		when(memberDao.findAllByKeys((List<Long>) any())).thenAnswer(new Answer<Map<Long, Member>>() {

			@Override
			public Map<Long, Member> answer(InvocationOnMock invocation) throws Throwable {
				List<Long> ids = (List<Long>) invocation.getArguments()[0];
				Map<Long, Member> members = new HashMap<Long, Member>();
				for (Long id : ids)
					members.put(id, new Member());
				return members;
			}
		});
	}

	@Given("A QuestionDao")
	public void initQuestionDao() {
		questionDao = mock(QuestionDao.class);
		when(questionDao.findAllByKeys((List<Long>) any())).thenReturn(new HashMap<Long, Question>());
	}

	MatchServiceImpl service;

	@Given("A MatchServiceImpl")
	public void initMatchServiceImp() {
		service = new MatchServiceImpl();
		service.leagueOrganizer = lo;
		service.memberDao = memberDao;
		timeProvider = new MockedTimeProvider();
		service.timeProvider = timeProvider;
		service.questionDao = questionDao;
	}

	Map<Long, Member> members;

	@Given("There are $number Members $list")
	public void initMembers(int number, List<String> list) {
		members = new HashMap<Long, Member>();
		for (int i = 0; i < list.size(); i++) {
			Long memberId = Long.parseLong(list.get(i));
			Member m = new Member();
			m.setAutoId(memberId);
			m.setDisplayName("user" + memberId);
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
		List<Long> qsIds = new ArrayList<Long>();
		for (int i = 0; i < number; i++)
			qsIds.add(new Long(i));
		currentMatch.setQuestionIds(qsIds);
	}

	int bufferedQuestionsCount = 0;
	MatchState matchState;

	@When("Member@$id request for Match State")
	public void requestMatchState(Long id) throws NotLoggedInException {
		setUpCurrentMember(members.get(id));
		matchState = service.getMatchState(1L, bufferedQuestionsCount);
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
			competitor.setMemberAutoId(id);
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

	@Given("Member@$id has answered $number questions")
	public void setUpCurrentMemberAnsweredQuestions(long id, int number) {
		currentMatch.getCompetitorForMember(members.get(id).getAutoId()).setPassedQuestionCount(number);
	}

	@Given("Member@$id finished the match")
	public void setUpMemberFinishedMatch(Long id) {
		currentMatch.getCompetitorForMember(members.get(id).getAutoId()).setFinishedMoment(1L);
	}

	@Given("Current Time is $stamp")
	public void setUpCurrentTime(long stamp) {
		timeProvider.setCurrentTimeStamp(stamp);
	}

	@Then("State Buffered Questions has $number questions")
	public void assertNumberOfBufferedQuestionsOfMatchState(int number) {
		assertEquals(matchState.getBufferedQuestions().size(), number);
	}

	@Then("State Completed Questions Count is $number")
	public void assertNumberOfCompletedQuestionsCount(int number) {
		assertEquals(matchState.getCompletedQuestionsCount(), number);
	}

	@Given("Match autoId is $id")
	public void setMatchAutoId(long id) {
		currentMatch.setAutoId(id);
	}

	@When("State matchAutoId is $id")
	public void assertMatchStateAutoId(long id) {
		assertEquals(matchState.getMatchAutoId(), id);
	}

	@Then("State alias is \"$alias\"")
	public void assertMatchStateAlias(String alias) {
		assertEquals(matchState.getMatchAlias(), alias);
	}

	@Given("Match alias is \"$alias\"")
	public void setMatchAlias(String alias) {
		currentMatch.setAlias(alias);
	}

	@Then("State contains map of $num members")
	public void assertStateContainMembersMap(int num) {
		assertEquals(matchState.getMembersMap().size(), 4);
	}
}
