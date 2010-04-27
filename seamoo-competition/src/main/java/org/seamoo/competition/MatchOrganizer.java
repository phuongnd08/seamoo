package org.seamoo.competition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.apache.commons.collections.list.TreeList;
import org.seamoo.cache.CacheWrapper;
import org.seamoo.cache.CacheWrapperFactory;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchAnswer;
import org.seamoo.entities.matching.MatchAnswerType;
import org.seamoo.entities.matching.MatchCandidate;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchEvent;
import org.seamoo.entities.matching.MatchEventType;
import org.seamoo.entities.matching.MatchPhase;
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
	public final long MAX_LOCK_WAIT_TIME = 5000L;

	@Autowired
	MemberDao memberDao;
	@Autowired
	MatchDao matchDao;
	@Autowired
	QuestionDao questionDao;
	@Autowired
	CacheWrapperFactory cacheWrapperFactory;
	/**
	 * Candidates for match
	 */
	/**
	 * List of matches that is either NOT_FORMED or FORMED but with number of
	 * competitors < MAX_CANDIDATE_PER_MATCH, mean that there's chances that a
	 * user can join the match
	 */
	private CacheWrapper<List> notFullWaitingMatches;
	/**
	 * List of matches that is FORMED and number of competitors
	 * >=MAX_CANDIDATE_PER_MATCH
	 */
	private CacheWrapper<List> fullWaitingMatches;

	private CacheWrapper<Match> getMatchWrapperByUUID(String uuid) {
		return cacheWrapperFactory.createCacheWrapper(Match.class, uuid);
	}

	private CacheWrapper<MatchCandidate> getCandidateWrapperByAutoId(Long autoId) {
		return cacheWrapperFactory.createCacheWrapper(MatchCandidate.class, autoId.toString());
	}

	boolean initialized;

	public MatchOrganizer() {
		initialized = false;
	}

	public static final String NOT_FULL_WAITING_MATCHES_KEY = "not-full-waiting-matches";
	public static final String FULL_WAITING_MATCHES_KEY = "full-waiting-matches";

	protected synchronized void initialize() {
		if (initialized)
			return;
		notFullWaitingMatches = cacheWrapperFactory.createCacheWrapper(List.class, NOT_FULL_WAITING_MATCHES_KEY);
		fullWaitingMatches = cacheWrapperFactory.createCacheWrapper(List.class, FULL_WAITING_MATCHES_KEY);
		initialized = true;
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
	private boolean isMatchFull(Match match) {
		return match.getCompetitors().size() == MAX_CANDIDATE_PER_MATCH;
	}

	protected static interface DoWhileListsLockedRunner {
		/**
		 * Perform a neccessary action while two waiting lists are locked.
		 * 
		 * @param fullList
		 * @param notFullList
		 * @return true if either list is modified. false if neither is modified
		 */
		boolean perform(List fullList, List notFullList);
	}

	protected synchronized void doWhileListsLocked(DoWhileListsLockedRunner runner) throws TimeoutException {
		fullWaitingMatches.lock(MAX_LOCK_WAIT_TIME);
		notFullWaitingMatches.lock(MAX_LOCK_WAIT_TIME);
		try {
			List fullList = fullWaitingMatches.getObject();
			if (fullList == null)
				fullList = new ArrayList();
			List notFullList = notFullWaitingMatches.getObject();
			if (notFullList == null)
				notFullList = new ArrayList();
			if (runner.perform(fullList, notFullList)) {
				fullWaitingMatches.putObject(fullList);
				notFullWaitingMatches.putObject(notFullList);
			}
		} finally {
			//always guarantee that lock will be released
			notFullWaitingMatches.unlock();
			fullWaitingMatches.unlock();
		}
	}

	private void recheckCompetitors(final Match match, List fullList, List notFullList) {
		boolean isMatchFullBeforeCheck = isMatchFull(match);
		for (int i = match.getCompetitors().size() - 1; i >= 0; i--) {
			MatchCompetitor competitor = match.getCompetitors().get(i);
			MatchCandidate candidate = getMatchCandidate(competitor.getMember().getAutoId());
			if (competitor.getFinishedMoment() == 0 && isDisconnected(candidate)) {
				match.addEvent(new MatchEvent(MatchEventType.LEFT, competitor.getMember().getAutoId()));
				match.getCompetitors().remove(i);
			}
		}
		if (isMatchFullBeforeCheck && !isMatchFull(match)) {
			// TODO Auto-generated method stub
			fullList.remove(match.getTemporalUUID());
			notFullList.add(match.getTemporalUUID());
		}
	}

	/**
	 * Find an available match (match that doesn't have enough member) or create
	 * a new match if necessary and assign member to the new created match
	 * 
	 * @param candidate
	 * @param notFullList
	 * @param fullList
	 * @return
	 * @throws TimeoutException
	 */
	private Match findAndAssignAvailableMatch(final MatchCandidate candidate, List fullList, List notFullList) {
		CacheWrapper<Match> matchWrapper = null;
		Match match = null;
		while (notFullList.size() > 0) {
			matchWrapper = getMatchWrapperByUUID((String) notFullList.get(0));
			match = matchWrapper.getObject();

			if (match == null) {
				// Cache of the match is corrupted
				notFullList.remove(0);
			} else if (shouldStarted(match)) {
				notFullList.remove(0);
				match = null;
			} else {
				recheckCompetitors(match, fullList, notFullList);
				break;
			}
		}
		if (match == null) {
			match = createNewMatch();
			notFullList.add(match.getTemporalUUID());
			matchWrapper = getMatchWrapperByUUID(match.getTemporalUUID());
		}
		MatchCompetitor competitor = new MatchCompetitor();
		competitor.setMember(memberDao.findByKey(candidate.getMemberAutoId()));
		match.addCompetitor(competitor);
		match.addEvent(new MatchEvent(MatchEventType.JOIN, competitor.getMember().getAutoId()));

		if (isMatchFull(match)) {
			fullList.add(match.getTemporalUUID());
			notFullList.remove(match.getTemporalUUID());
		}

		return match;
	}

	private Match createNewMatch() {
		Match match = EntityFactory.newMatch();
		match.setPhase(MatchPhase.NOT_FORMED);
		match.setQuestions(questionDao.getRandomQuestions(QUESTION_PER_MATCH));
		match.setTemporalUUID(UUID.randomUUID().toString());
		return match;
	}

	private void updateLastSeen(MatchCandidate candidate) {
		candidate.setLastSeenMoment(TimeStampProvider.getCurrentTimeMilliseconds());
	}

	private void recacheCandidate(MatchCandidate candidate) {
		CacheWrapper<MatchCandidate> candidateWrapper = getCandidateWrapperByAutoId(candidate.getMemberAutoId());
		candidateWrapper.putObject(candidate);
	}

	private boolean isDisconnected(MatchCandidate candidate) {
		return candidate.getLastSeenMoment() + CANDIDATE_ACTIVE_PERIOD < TimeStampProvider.getCurrentTimeMilliseconds();
	}

	private MatchCandidate getMatchCandidate(Long userAutoId) {
		CacheWrapper<MatchCandidate> candidateWrapper = getCandidateWrapperByAutoId(userAutoId);
		MatchCandidate candidate = candidateWrapper.getObject();
		if (candidate == null) {
			candidate = new MatchCandidate();
			candidate.setMemberAutoId(userAutoId);
		}
		return candidate;
	}

	private Match getMatchForCandidate(MatchCandidate candidate, List fullList, List notFullList) {
		Match match = null;

		if (candidate.getCurrentMatchUUID() != null && !isDisconnected(candidate)) {
			match = getMatchWrapperByUUID(candidate.getCurrentMatchUUID()).getObject();
		}

		// Deal with both situation: When user is not allocated a match or when
		// cache of match is corrupted
		if (match != null) {
			recheckCompetitors(match, fullList, notFullList);
		} else {
			match = findAndAssignAvailableMatch(candidate, fullList, notFullList);
			candidate.setCurrentMatchUUID(match.getTemporalUUID());
		}
		return match;
	}

	private void recheckMatchPhase(Match match, List fullList, List notFullList) {
		if (match.getPhase() == MatchPhase.NOT_FORMED && match.getCompetitors().size() >= MIN_CANDIDATE_PER_MATCH) {
			match.setPhase(MatchPhase.FORMED);
			match.setFormedMoment(TimeStampProvider.getCurrentTimeMilliseconds());
			match.setStartedMoment(TimeStampProvider.getCurrentTimeMilliseconds() + MATCH_COUNTDOWN_TIME);
			match.setEndedMoment(match.getStartedMoment() + MATCH_TIME);
		} else if (match.getPhase() == MatchPhase.FORMED) {
			if (match.getCompetitors().size() < MIN_CANDIDATE_PER_MATCH) {
				match.setPhase(MatchPhase.NOT_FORMED);
			} else if (match.getStartedMoment() <= TimeStampProvider.getCurrentTimeMilliseconds()) {
				startMatch(match, fullList, notFullList);
			}
		} else if (match.getPhase() == MatchPhase.PLAYING
				&& match.getEndedMoment() <= TimeStampProvider.getCurrentTimeMilliseconds()) {
			finishMatch(match);
		}
	}

	private void startMatch(Match match, List fullList, List notFullList) {
		// TODO Auto-generated method stub
		match.setPhase(MatchPhase.PLAYING);
		match.addEvent(new MatchEvent(MatchEventType.STARTED));
		if (!notFullList.remove(match.getTemporalUUID()))
			fullList.remove(match.getTemporalUUID());
	}

	private void checkMatchFinished(Match match) {
		for (MatchCompetitor competitor : match.getCompetitors()) {
			if (competitor.getFinishedMoment() == 0)
				return;
		}
		finishMatch(match);// all answers submitted/ignored
	}

	/**
	 * Change a match into finished state and persist it into db
	 * 
	 * @param match
	 */
	private void finishMatch(Match match) {
		match.setPhase(MatchPhase.FINISHED);
		match.addEvent(new MatchEvent(MatchEventType.FINISHED));
		for (MatchCompetitor competitor : match.getCompetitors()) {
			if (competitor.getFinishedMoment() == 0)
				competitor.setFinishedMoment(TimeStampProvider.getCurrentTimeMilliseconds());
		}
		rank(match);
		prepareMatchForPersistence(match);
		matchDao.persist(match);
	}

	/**
	 * Try to associate necessary entity of match so that persistence can
	 * maintain a consistent link between entities in datastore
	 * 
	 * @param match
	 */
	private void prepareMatchForPersistence(Match match) {
	}

	private void rank(Match match) {
		// TODO Auto-generated method stub
		List<MatchCompetitor> competitors = new ArrayList<MatchCompetitor>(match.getCompetitors());
		Collections.sort(competitors, new Comparator<MatchCompetitor>() {

			@Override
			public int compare(MatchCompetitor c1, MatchCompetitor c2) {
				// TODO Auto-generated method stub
				int result = -Double.compare(c1.getTotalScore(), c2.getTotalScore());
				if (result == 0) {
					if (c1.getFinishedMoment() > c2.getFinishedMoment())
						return 1;
					else if (c1.getFinishedMoment() == c2.getFinishedMoment())
						return 0;
					else
						return -1;
				}
				return result;
			}
		});
		for (int i = 0; i < competitors.size(); i++) {
			competitors.get(i).setRank(i + 1);
		}
	}

	/**
	 * A Member try to get his current match
	 * 
	 * @param userAutoId
	 * @return
	 * @throws TimeoutException
	 */
	public Match getMatchForUser(Long userAutoId) {
		initialize();
		final MatchCandidate candidate = getMatchCandidate(userAutoId);
		if (isDisconnected(candidate))
			candidate.setCurrentMatchUUID(null);
		updateLastSeen(candidate);
		final Match[] matches = new Match[1];
		try {
			doWhileListsLocked(new DoWhileListsLockedRunner() {

				@Override
				public boolean perform(List fullList, List notFullList) {
					matches[0] = getMatchForCandidate(candidate, fullList, notFullList);
					// check for which phase should match be now
					recheckMatchPhase(matches[0], fullList, notFullList);
					/*
					 * The match has gone through several changes. Put it back
					 * to cache
					 */
					CacheWrapper<Match> matchWrapper = getMatchWrapperByUUID(matches[0].getTemporalUUID());
					matchWrapper.putObject(matches[0]);

					return true;
				}
			});
		} catch (TimeoutException e) {
			// Cannot acquire enough lock to find match for user. Give up
			return null;
		}

		recacheCandidate(candidate);
		return matches[0];
	}

	/**
	 * A Member trying to escape his current match. He may want to return to his
	 * match later
	 * 
	 * @param userAutoId
	 */
	public void escapeCurrentMatch(Long userAutoId) {
		initialize();
		getMatchCandidate(userAutoId).setCurrentMatchUUID(null);
	}

	private void addMatchAnswer(Long userAutoId, int questionOrder, MatchAnswer answer) {
		MatchCandidate candidate = getMatchCandidate(userAutoId);
		if (candidate.getCurrentMatchUUID() == null || isDisconnected(candidate)) {
			System.err.println("Cannot find a suitable match for user. Answer discarded");
			return;
		}
		CacheWrapper<Match> matchWrapper = getMatchWrapperByUUID(candidate.getCurrentMatchUUID());
		Match match = matchWrapper.getObject();
		if (match == null) {
			System.err.println("Cannot find match associated with user. Probably cache is corrupted. Answer discarded");
			return;
		}

		List<MatchCompetitor> competitors = match.getCompetitors();
		MatchCompetitor competitor = match.getCompetitorForMember(candidate.getMemberAutoId());
		if (competitor == null)
			throw new RuntimeException("Competitor disappeared mysteriously");
		switch (answer.getType()) {
		case IGNORED:
			match.addEvent(new MatchEvent(MatchEventType.IGNORE_QUESTION, candidate.getMemberAutoId(), questionOrder));
			break;
		case SUBMITTED:
			match.addEvent(new MatchEvent(MatchEventType.ANSWER_QUESTION, candidate.getMemberAutoId(), questionOrder));
			double score = match.getQuestions().get(questionOrder - 1).getScore(answer.getContent());
			answer.setScore(score);
			answer.setCorrect(score > 0);
			break;
		}

		List<MatchAnswer> answers = competitor.getAnswers();
		int insertPos = questionOrder - 1;
		if (answers.size() < insertPos) {
			for (int i = answers.size(); i < insertPos; i++)
				competitor.addAnswer(new MatchAnswer(MatchAnswerType.IGNORED, null));
		}
		if (insertPos < answers.size()) {
			System.err.println("Late received answer at " + questionOrder + " discarded");
		} else
			competitor.addAnswer(answer);

		if (competitor.getPassedQuestionCount() == match.getQuestions().size()) {
			competitor.setFinishedMoment(TimeStampProvider.getCurrentTimeMilliseconds());
			checkMatchFinished(match);
		}

		matchWrapper.putObject(match);

		updateLastSeen(candidate);
		recacheCandidate(candidate);
	}

	/**
	 * Competitor submit an answer
	 * 
	 * 
	 * @param userAutoId
	 * @param questionOrder
	 *            Must be in 1-based index
	 * @param answer
	 */
	public void submitAnswer(Long userAutoId, int questionOrder, String answer) {
		initialize();
		addMatchAnswer(userAutoId, questionOrder, new MatchAnswer(MatchAnswerType.SUBMITTED, answer));
	}

	/**
	 * Competitor ignore a question in match
	 * 
	 * @param userAutoId
	 * @param questionOrder
	 *            Must be in 1-based index
	 */
	public void ignoreQuestion(Long userAutoId, int questionOrder) {
		initialize();
		addMatchAnswer(userAutoId, questionOrder, new MatchAnswer(MatchAnswerType.IGNORED, null));
	}

}
