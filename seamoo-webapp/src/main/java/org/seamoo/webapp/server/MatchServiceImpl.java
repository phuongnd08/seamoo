package org.seamoo.webapp.server;

import java.util.ArrayList;
import java.util.List;

import org.seamoo.competition.MatchOrganizer;
import org.seamoo.competition.TimeStampProvider;
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
	MatchOrganizer matchOrganizer;

	int BUFFERED_QUESTION_BLOCK = 5;
	int BUFFERED_QUESTION_REFILL_THRESHOLD = 2;

	private Member getInjectedMember() {
		return (Member) ServletUtils.getRequest().getAttribute(MemberInjectionFilter.MEMBER_FIELD);
	}

	@Override
	public MatchState getMatchState(int bufferedQuestionsCount, int bufferredEventsCount) {
		Member member = getInjectedMember();
		Match match = matchOrganizer.getMatchForUser(member.getAutoId());
		synchronized (match) {
			//make sure not change on match during the process of generating RPC object
			MatchState matchState = new MatchState();
			matchState.setCompetitors(match.getCompetitors());
			matchState.setQuestionsCount(match.getQuestions().size());
			MatchCompetitor competitor = match.getCompetitorForMember(member);
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
			if (bufferedQuestionsCount <= match.getEvents().size()) {
				matchState.setBufferedEvents(new ArrayList<MatchEvent>(match.getEvents().subList(bufferredEventsCount,
						match.getEvents().size())));
			} else
				System.err.println("Error: bufferedEvents from " + member.getDisplayName() + " > match.events.size");
			matchState.setMatchAutoId(match.getAutoId() != null ? match.getAutoId().longValue() : 0);
			matchState.setMatchAlias(match.getAlias());
			return matchState;
		}
	}

	@Override
	public void ignoreQuestion(int questionOrder) {
		Member member = getInjectedMember();
		matchOrganizer.ignoreQuestion(member.getAutoId(), questionOrder);

	}

	@Override
	public void submitAnswer(int questionOrder, String answer) {
		Member member = getInjectedMember();
		matchOrganizer.submitAnswer(member.getAutoId(), questionOrder, answer);
	}
}
