package org.seamoo.competition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchAnswer;
import org.seamoo.entities.matching.MatchAnswerType;
import org.seamoo.entities.matching.MatchCandidate;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.persistence.daos.MemberDao;
import org.seamoo.persistence.matching.daos.MatchDao;
import org.seamoo.persistence.question.daos.QuestionDao;
import org.springframework.beans.factory.annotation.Autowired;

public class MatchOrganizer {
	public final long CANDIDATE_ACTIVE_PERIOD = 5000;/*
													 * 5000 milliseconds or 5
													 * seconds
													 */
	public final long MATCH_COUNTDOWN_TIME = 10000;
	public final long MAX_CANDIDATE_PER_MATCH = 4;
	public final long MIN_CANDIDATE_PER_MATCH = 2;
	public final int QUESTION_PER_MATCH = 20;
	public final long MATCH_TIME = 120000;

	@Autowired
	MemberDao memberDao;
	@Autowired
	MatchDao matchDao;
	@Autowired
	QuestionDao questionDao;
	/**
	 * Candidates for match
	 */
	Map<Long, MatchCandidate> candidates;
	/**
	 * List of matches that is either NOT_FORMED or FORMED, mean that there's
	 * chances that a user can join the match
	 */
	private List<Match> waitingMatches;

	public MatchOrganizer() {
		candidates = new HashMap<Long, MatchCandidate>();
		waitingMatches = new ArrayList<Match>();
	}

	private boolean shouldStarted(Match match) {
		return match.getPhase() == MatchPhase.FORMED && match.getStartedMoment() < TimeStampProvider.getCurrentTimeMilliseconds();
	}

	private boolean canJoin(Match match) {
		return match.getCompetitors().size() < MAX_CANDIDATE_PER_MATCH;
	}

	/**
	 * Get rid of disconnected candidate
	 * 
	 * @param match
	 */
	private void recheckMatchCompetitors(Match match) {
		for (int i = match.getCompetitors().size() - 1; i >= 0; i--) {
			MatchCompetitor competitor = match.getCompetitors().get(i);
			MatchCandidate candidate = getMatchCandidate(competitor.getMember().getAutoId());
			if (competitor.getFinishedMoment() == 0 && isDisconnected(candidate)) {
				match.getCompetitors().remove(i);
			}
		}
	}

	/**
	 * Find an available match (match that doesn't have enough member) or create
	 * a new match if necessary and assign member to the new created match
	 * 
	 * @param candidate
	 * @return
	 */
	private Match findAndAssignAvailableMatch(MatchCandidate candidate) {
		synchronized (waitingMatches) {
			// get rid of should stared match
			for (int i = waitingMatches.size() - 1; i >= 0; i--) {
				if (shouldStarted(waitingMatches.get(i))) {
					waitingMatches.remove(i);
				}
			}
			Match match = null;
			for (Match m : waitingMatches) {
				recheckMatchCompetitors(m);
				if (canJoin(m)) {
					match = m;
					break;
				}
			}
			if (match == null) {
				match = createNewMatch();
			}
			MatchCompetitor competitor = new MatchCompetitor();
			competitor.setMember(candidate.getMember());
			match.getCompetitors().add(competitor);
			return match;
		}
	}

	private Match createNewMatch() {
		Match match = EntityFactory.newMatch();
		match.setPhase(MatchPhase.NOT_FORMED);
		match.setQuestions(questionDao.getRandomQuestions(QUESTION_PER_MATCH));
		waitingMatches.add(match);
		return match;
	}

	private void updateLastSeen(MatchCandidate candidate) {
		candidate.setLastSeenMoment(TimeStampProvider.getCurrentTimeMilliseconds());
	}

	private boolean isDisconnected(MatchCandidate candidate) {
		return candidate.getLastSeenMoment() + CANDIDATE_ACTIVE_PERIOD < TimeStampProvider.getCurrentTimeMilliseconds();
	}

	private MatchCandidate getMatchCandidate(Long userAutoId) {
		MatchCandidate candidate;
		if (candidates.containsKey(userAutoId)) {
			candidate = candidates.get(userAutoId);
		} else {
			candidate = new MatchCandidate();
			candidate.setMember(memberDao.findByAutoId(userAutoId));
			candidates.put(userAutoId, candidate);
		}
		return candidate;
	}

	private Match getMatchForCandidate(MatchCandidate candidate) {
		if (candidate.getCurrentMatch() == null || isDisconnected(candidate)) {
			Match match = findAndAssignAvailableMatch(candidate);
			candidate.setCurrentMatch(match);
			return match;
		} else {
			recheckMatchCompetitors(candidate.getCurrentMatch());
			return candidate.getCurrentMatch();
		}
	}

	private void recheckMatchPhase(Match match) {
		if (match.getPhase() == MatchPhase.NOT_FORMED && match.getCompetitors().size() >= MIN_CANDIDATE_PER_MATCH) {
			match.setPhase(MatchPhase.FORMED);
			match.setFormedMoment(TimeStampProvider.getCurrentTimeMilliseconds());
			match.setStartedMoment(TimeStampProvider.getCurrentTimeMilliseconds() + MATCH_COUNTDOWN_TIME);
			match.setEndedMoment(match.getStartedMoment() + MATCH_TIME);
		} else if (match.getPhase() == MatchPhase.FORMED) {
			if (match.getCompetitors().size() < MIN_CANDIDATE_PER_MATCH) {
				match.setPhase(MatchPhase.NOT_FORMED);
			} else if (match.getStartedMoment() <= TimeStampProvider.getCurrentTimeMilliseconds()) {
				match.setPhase(MatchPhase.PLAYING);
				synchronized (waitingMatches) {
					waitingMatches.remove(match);
				}
			}
		} else if (match.getPhase() == MatchPhase.PLAYING
				&& match.getEndedMoment() <= TimeStampProvider.getCurrentTimeMilliseconds()) {
			finishMatch(match);
		}
	}

	/**
	 * Change a match into finished state and persist it into db
	 * 
	 * @param match
	 */
	private void finishMatch(Match match) {
		match.setPhase(MatchPhase.FINISHED);
		matchDao.persist(match);
	}

	/**
	 * A Member try to get his current match
	 * 
	 * @param userAutoId
	 * @return
	 */
	public Match getMatchForUser(Long userAutoId) {
		MatchCandidate candidate = getMatchCandidate(userAutoId);
		Match match = getMatchForCandidate(candidate);
		updateLastSeen(candidate);
		// check for various phase of match
		recheckMatchPhase(match);
		return match;
	}

	/**
	 * A Member trying to escape his current match. He may want to return to his
	 * match later
	 * 
	 * @param userAutoId
	 */
	public void escapeCurrentMatch(Long userAutoId) {
		getMatchCandidate(userAutoId).setCurrentMatch(null);
	}

	private void checkMatchFinished(Match match) {
		for (MatchCompetitor competitor : match.getCompetitors()) {
			if (competitor.getFinishedMoment() == 0)
				return;
		}
		finishMatch(match);// all answers submitted/ignored
	}

	private void addMatchAnswer(Long userAutoId, int questionOrder, MatchAnswer answer) {
		MatchCandidate candidate = getMatchCandidate(userAutoId);
		Match match = getMatchForCandidate(candidate);
		List<MatchCompetitor> competitors = match.getCompetitors();
		MatchCompetitor competitor = null;
		for (int i = 0; i < competitors.size(); i++) {
			if (competitors.get(i).getMember().getAutoId() == candidate.getMember().getAutoId()) {
				competitor = competitors.get(i);
				break;
			}
		}
		if (competitor == null)
			throw new RuntimeException("Competitor disappeared mysteriously");
		competitor.addAnswer(questionOrder, answer);
		if (competitor.getPassedQuestionCount() == match.getQuestions().size()) {
			competitor.setFinishedMoment(TimeStampProvider.getCurrentTimeMilliseconds());
			checkMatchFinished(match);
		}
		updateLastSeen(candidate);
	}

	/**
	 * Competitor submit an answer
	 * 
	 * @param userAutoId
	 * @param questionOrder
	 * @param answer
	 */
	public void submitAnswer(Long userAutoId, int questionOrder, String answer) {
		addMatchAnswer(userAutoId, questionOrder, new MatchAnswer(MatchAnswerType.SUBMITTED, answer));
	}

	/**
	 * Competitor ignore a question in match
	 * 
	 * @param userAutoId
	 * @param questionOrder
	 */
	public void ignoreQuestion(Long userAutoId, int questionOrder) {
		addMatchAnswer(userAutoId, questionOrder, new MatchAnswer(MatchAnswerType.IGNORED, null));
	}

}
