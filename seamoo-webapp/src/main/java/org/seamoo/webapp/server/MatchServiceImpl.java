package org.seamoo.webapp.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.seamoo.competition.LeagueOrganizer;
import org.seamoo.competition.MatchOrganizer;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.League;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchEvent;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.matching.MatchState;
import org.seamoo.entities.question.Question;
import org.seamoo.utils.TimeProvider;
import org.seamoo.webapp.client.shared.NotLoggedInException;
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
	@Autowired
	LeagueDao leagueDao;
	@Autowired
	QuestionDao questionDao;

	TimeProvider timeProvider = TimeProvider.DEFAULT;

	int BUFFERED_QUESTION_BLOCK = 5;
	int BUFFERED_QUESTION_REFILL_THRESHOLD = 2;

	private Member getInjectedMember() {
		return (Member) ServletUtils.getRequest().getAttribute(MemberInjectionFilter.MEMBER_FIELD);
	}

	@Override
	public MatchState getMatchState(Long leagueId, int bufferedQuestionsCount)
			throws NotLoggedInException {
		Member member = getInjectedMember();
		if (member == null) {
			throw new NotLoggedInException();
		}
		MatchOrganizer matchOrganizer = leagueOrganizer.getMatchOrganizer(leagueId);
		Match match;
		match = matchOrganizer.getMatchForUser(member.getAutoId());

		MatchState matchState = new MatchState();
		matchState.setCompetitors(match.getCompetitors());
		matchState.setQuestionsCount(match.getQuestionIds().size());
		MatchCompetitor competitor = match.getCompetitorForMember(member.getAutoId());
		matchState.setCompletedQuestionsCount(competitor.getPassedQuestionCount());
		// assign buffered questions when user is in the middle of the match
		// and
		// in need of questions
		if (match.getPhase() == MatchPhase.PLAYING) {
			if (bufferedQuestionsCount < matchState.getQuestionsCount()
					&& bufferedQuestionsCount - competitor.getPassedQuestionCount() <= BUFFERED_QUESTION_REFILL_THRESHOLD) {
				matchState.setBufferedQuestionsFrom(bufferedQuestionsCount);
				List<Question> bufferedQuestions = getOptimalQuestionBuffer(bufferedQuestionsCount,
						competitor.getPassedQuestionCount(), questionDao.findAllByKeys(match.getQuestionIds()));
				matchState.getBufferedQuestions().addAll(bufferedQuestions);
			}
		}
		// set the correct remaining time
		if (match.getPhase() == MatchPhase.FORMED) {
			matchState.setRemainingPeriod(match.getStartedMoment() - timeProvider.getCurrentTimeStamp());
		} else if (match.getPhase() == MatchPhase.PLAYING) {
			matchState.setRemainingPeriod(match.getEndedMoment() - timeProvider.getCurrentTimeStamp());
		}
		// set the correct phase for current member
		if (match.getPhase() == MatchPhase.PLAYING && competitor.getFinishedMoment() != 0) {
			matchState.setPhase(MatchPhase.YOU_FINISHED);
		} else
			matchState.setPhase(match.getPhase());

		matchState.setMatchAutoId(match.getAutoId() != null ? match.getAutoId().longValue() : 0);
		matchState.setLeagueAutoId(match.getLeagueAutoId());
		matchState.setMatchAlias(match.getAlias());
		return matchState;
	}

	private List<Question> getOptimalQuestionBuffer(int bufferedQuestionsCount, int passedQuestionCount, List<Question> questions) {
		int lowRange = bufferedQuestionsCount;
		int highRange = bufferedQuestionsCount + BUFFERED_QUESTION_BLOCK;
		while (highRange <= passedQuestionCount + BUFFERED_QUESTION_REFILL_THRESHOLD) {
			if (highRange < questions.size())
				highRange += BUFFERED_QUESTION_BLOCK;
			else
				break;
		}

		List<Question> bufferedQuestions = questions.subList(lowRange, highRange);
		return bufferedQuestions;
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

	@Override
	public League escapeCurrentMatch(Long leagueId) {
		// TODO Auto-generated method stub
		MatchOrganizer matchOrganizer = leagueOrganizer.getMatchOrganizer(leagueId);
		Member member = getInjectedMember();
		matchOrganizer.escapeCurrentMatch(member.getAutoId());
		return leagueDao.findByKey(leagueId);
	}
}
