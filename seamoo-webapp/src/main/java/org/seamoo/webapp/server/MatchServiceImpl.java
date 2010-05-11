package org.seamoo.webapp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.seamoo.competition.LeagueOrganizer;
import org.seamoo.competition.MatchOrganizer;
import org.seamoo.competition.TimeStampProvider;
import org.seamoo.daos.MemberDao;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchEvent;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.matching.MatchState;
import org.seamoo.entities.question.Question;
import org.seamoo.webapp.client.user.MatchService;
import org.seamoo.webapp.filters.MemberInjectionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.workingonit.gwtbridge.ServletUtils;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MatchServiceImpl extends RemoteServiceServlet implements MatchService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4763302000027924061L;
	@Autowired
	LeagueOrganizer leagueOrganizer;
	@Autowired
	MemberDao memberDao;

	int BUFFERED_QUESTION_BLOCK = 5;
	int BUFFERED_QUESTION_REFILL_THRESHOLD = 2;

	private Member getInjectedMember() {
		return (Member) ServletUtils.getRequest().getAttribute(MemberInjectionFilter.MEMBER_FIELD);
	}

	@Override
	public MatchState getMatchState(Long leagueId, int bufferedQuestionsCount, int bufferredEventsCount) {
		Member member = getInjectedMember();
		MatchOrganizer matchOrganizer = leagueOrganizer.getMatchOrganizer(leagueId);
		Match match;
		try {
			match = matchOrganizer.getMatchForUser(member.getAutoId());
		} catch (TimeoutException e1) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e1);
		}
		// make sure not change on match during the process of generating
		// RPC object
		MatchState matchState = new MatchState();
		matchState.setCompetitors(match.getCompetitors());
		matchState.setQuestionsCount(match.getQuestions().size());
		MatchCompetitor competitor = match.getCompetitorForMember(member.getAutoId());
		matchState.setCompletedQuestionsCount(competitor.getPassedQuestionCount());
		// assign buffered questions when user is in the middle of the match
		// and
		// in need of questions
		if (match.getPhase() == MatchPhase.PLAYING && bufferedQuestionsCount < matchState.getQuestionsCount()
				&& bufferedQuestionsCount - competitor.getPassedQuestionCount() <= BUFFERED_QUESTION_REFILL_THRESHOLD) {
			matchState.setBufferedQuestionsFrom(bufferedQuestionsCount);
			List<Question> bufferedQuestions = match.getQuestions().subList(bufferedQuestionsCount,
					bufferedQuestionsCount + BUFFERED_QUESTION_BLOCK);
			matchState.getBufferedQuestions().addAll(bufferedQuestions);
		}
		// set the correct remaining time
		if (match.getPhase() == MatchPhase.FORMED) {
			matchState.setRemainingPeriod(match.getStartedMoment() - TimeStampProvider.getCurrentTimeMilliseconds());
		} else if (match.getPhase() == MatchPhase.PLAYING) {
			matchState.setRemainingPeriod(match.getEndedMoment() - TimeStampProvider.getCurrentTimeMilliseconds());
		}
		// set the correct phase for current member
		if (match.getPhase() == MatchPhase.PLAYING && competitor.getFinishedMoment() != 0) {
			matchState.setPhase(MatchPhase.YOU_FINISHED);
		} else
			matchState.setPhase(match.getPhase());
		// assign buffered events
		assert bufferredEventsCount <= match.getEvents().size();
		matchState.setBufferedEvents(new ArrayList<MatchEvent>(match.getEvents().subList(bufferredEventsCount,
				match.getEvents().size())));
		// bind member to event using memberAutoId
		for (MatchEvent e : matchState.getBufferedEvents()) {
			if (e.getMemberAutoId() != null)
				e.setMember(memberDao.findByKey(e.getMemberAutoId()));
		}
		matchState.setMatchAutoId(match.getAutoId() != null ? match.getAutoId().longValue() : 0);
		matchState.setMatchAlias(match.getAlias());
		return matchState;
	}

	@Override
	public void ignoreQuestion(Long leagueId, int questionOrder) {
		MatchOrganizer matchOrganizer = leagueOrganizer.getMatchOrganizer(leagueId);
		Member member = getInjectedMember();
		try {
			matchOrganizer.ignoreQuestion(member.getAutoId(), questionOrder);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}

	}

	@Override
	public void submitAnswer(Long leagueId, int questionOrder, String answer) {
		MatchOrganizer matchOrganizer = leagueOrganizer.getMatchOrganizer(leagueId);
		Member member = getInjectedMember();
		try {
			matchOrganizer.submitAnswer(member.getAutoId(), questionOrder, answer);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}
