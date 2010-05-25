package org.seamoo.competition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.seamoo.cache.RemoteCompositeObject;
import org.seamoo.cache.RemoteCounter;
import org.seamoo.cache.RemoteObject;
import org.seamoo.cache.RemoteObjectFactory;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchAnswer;
import org.seamoo.entities.matching.MatchAnswerType;
import org.seamoo.entities.matching.MatchCandidate;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.question.Question;
import org.seamoo.utils.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.repackaged.com.google.common.collect.Lists;

public class MatchOrganizer {

	public static interface EventListener {
		void finishMatch(Match match);
	}

	@Autowired
	MemberDao memberDao;
	@Autowired
	MatchDao matchDao;
	@Autowired
	QuestionDao questionDao;
	@Autowired
	RemoteObjectFactory cacheWrapperFactory;
	@Autowired
	TimeProvider timeProvider = TimeProvider.DEFAULT;

	private List<EventListener> listeners;
	private RemoteCounter globalCompetitorSlotCounter;

	MatchOrganizerSettings settings;

	/**
	 * Candidates for match
	 */
	public static final String REMOTE_MAP_KEY_PREFIX = "RemoteMap";
	Map<Long, RemoteMatch> remoteMatchMap;

	private RemoteMatch getRemoteMatch(long key) {
		synchronized (remoteMatchMap) {
			RemoteMatch m = remoteMatchMap.get(key);
			if (m == null) {
				RemoteCompositeObject rco = cacheWrapperFactory.createRemoteCompositeObject(REMOTE_MAP_KEY_PREFIX,
						String.valueOf(key));
				m = new RemoteMatch(rco);
				m.setKey(key);
				remoteMatchMap.put(key, m);
			}
			return m;
		}
	}

	private RemoteObject<MatchCandidate> getCandidateRemoteObject(Long memberAutoId) {
		return cacheWrapperFactory.createRemoteObject(MatchCandidate.class, memberAutoId.toString());
	}

	private long nextGlobalCompetitorSlot() {
		return globalCompetitorSlotCounter.inc();
	}

	boolean initialized;
	Long leagueId;

	public MatchOrganizer(Long leagueId) {
		this(leagueId, new MatchOrganizerSettings());
	}

	public MatchOrganizer(Long leagueId, MatchOrganizerSettings settings) {
		initialized = false;
		this.leagueId = leagueId;
		this.settings = settings;
		this.listeners = new ArrayList<EventListener>();
	}

	public static final String MATCH_ORGANIZER_COUNTER_CATEGORY = "match-organizer-counter";
	public static final String COMPETITOR_SLOT_COUNTER_KEY = "competitor-slot";
	public static final long COMPETITOR_SLOT_COUNTER_INITIAL = 0;

	protected synchronized void initialize() {
		if (initialized)
			return;
		globalCompetitorSlotCounter = cacheWrapperFactory.createRemoteCounter(MATCH_ORGANIZER_COUNTER_CATEGORY,
				COMPETITOR_SLOT_COUNTER_KEY, COMPETITOR_SLOT_COUNTER_INITIAL);
		remoteMatchMap = new HashMap<Long, RemoteMatch>();
		initialized = true;
	}

	private boolean isStarted(RemoteMatch match) {
		return match.getReadyMoment() + settings.getMatchCountDownTime() <= timeProvider.getCurrentTimeStamp();
	}

	private boolean isFinishedMatch(RemoteMatch match) {
		return match.getFinishedMoment() != 0
				|| match.getReadyMoment() + settings.getMatchCountDownTime() + settings.getMatchTime() <= timeProvider.getCurrentTimeStamp();
	}

	private RemoteMatch assignMatch(MatchCandidate candidate) {
		long competitorSlotId = nextGlobalCompetitorSlot();
		long matchId = competitorSlotId % 4 == 0 ? competitorSlotId / 4 : (competitorSlotId / 4) + 1;
		RemoteMatch match = getRemoteMatch(matchId);
		int competitorSlot = match.acquireCompetitorSlot();
		MatchCompetitor competitor = new MatchCompetitor();
		competitor.setMemberAutoId(candidate.getMemberAutoId());
		RemoteObject<MatchCompetitor> remoteCompetitor = match.getCompetitorSlot(competitorSlot);
		remoteCompetitor.putObject(competitor);
		candidate.setRemoteCompetitorSlot(competitorSlot);
		if (competitorSlot == 1) {
			List<Question> questions = questionDao.getRandomQuestions(leagueId, settings.getQuestionPerMatch());
			List<Long> questionIds = new ArrayList<Long>();
			for (Question q : questions)
				questionIds.add(q.getAutoId());
			match.setQuestionIds(questionIds.toArray(new Long[settings.getQuestionPerMatch()]));
		} else if (competitorSlot == 2) {
			match.setReadyMoment(timeProvider.getCurrentTimeStamp());
		} else {
			if (isStarted(match))
				// Match is already started, return null so that user may request for another match
				return null;
		}
		return match;
	}

	private void checkMatchFinished(RemoteMatch match) {
		for (Object o : match.getCompetitors()) {
			MatchCompetitor competitor = (MatchCompetitor) o;
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
	private void finishMatch(RemoteMatch remoteMatch) {
		long currentTimeStamp = timeProvider.getCurrentTimeStamp();
		Match match = toMatch(remoteMatch);
		match.setEndedMoment(currentTimeStamp);
		for (MatchCompetitor competitor : match.getCompetitors()) {
			if (competitor.getFinishedMoment() == 0)
				competitor.setFinishedMoment(timeProvider.getCurrentTimeStamp());
		}
		rank(match);
		prepareMatchForPersistence(match);
		for (EventListener listener : listeners)
			listener.finishMatch(match);
		matchDao.persist(match);
		remoteMatch.setDbKey(match.getAutoId());
		remoteMatch.setFinishedMoment(currentTimeStamp);
	}

	/**
	 * Try to associate necessary entity of match so that persistence can maintain a consistent link between entities in datastore
	 * 
	 * @param match
	 */
	private void prepareMatchForPersistence(Match match) {
		// Would not need to do anything since Twig-persist is smart enought to
		// re-link all objects before persisting them
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
	public Match getMatchForUser(final Long userAutoId) {
		initialize();
		RemoteObject<MatchCandidate> candidateWrapper = getCandidateRemoteObject(userAutoId);
		MatchCandidate candidate = candidateWrapper.getObject();
		if (candidate == null) {
			candidate = new MatchCandidate();
			candidate.setMemberAutoId(userAutoId);
		}

		RemoteMatch remoteMatch = null;
		if (candidate.getRemoteMatchKey() != null) {
			remoteMatch = getRemoteMatch(candidate.getRemoteMatchKey());
		}
		while (remoteMatch == null) {
			remoteMatch = assignMatch(candidate);
			if (remoteMatch != null) {
				candidate.setRemoteMatchKey(remoteMatch.getKey());
				candidateWrapper.putObject(candidate);
			}
		}
		return toMatch(remoteMatch);
	}

	private Match toMatch(RemoteMatch remoteMatch) {
		Match match = new Match();
		long readyMoment = remoteMatch.getReadyMoment();
		match.setFormedMoment(readyMoment);
		match.setStartedMoment(readyMoment + settings.getMatchCountDownTime());
		match.setEndedMoment(match.getStartedMoment() + settings.getMatchTime());
		if (readyMoment != 0) {
			if (match.getStartedMoment() > timeProvider.getCurrentTimeStamp()) {
				match.setPhase(MatchPhase.FORMED);
			} else if (match.getEndedMoment() > timeProvider.getCurrentTimeStamp() && remoteMatch.getFinishedMoment() == 0) {
				match.setPhase(MatchPhase.PLAYING);
			} else
				match.setPhase(MatchPhase.FINISHED);
		} else
			match.setPhase(MatchPhase.NOT_FORMED);

		for (Object c : remoteMatch.getCompetitors()) {
			match.addCompetitor((MatchCompetitor) c);
		}

		match.setDescription("Match #" + remoteMatch.getKey().toString());// expose match information for testing

		match.setQuestionIds(Lists.newArrayList(remoteMatch.getQuestionIds()));

		return match;
	}

	/**
	 * A Member trying to escape his current match. He may want to return to his match later
	 * 
	 * @param userAutoId
	 */
	public void escapeCurrentMatch(Long userAutoId) {
		initialize();
		RemoteObject<MatchCandidate> candidateWrapper = getCandidateRemoteObject(userAutoId);
		candidateWrapper.putObject(null);
	}

	private void addMatchAnswer(Long memberAutoId, int questionOrder, MatchAnswer answer) {
		RemoteObject<MatchCandidate> remoteCandidate = getCandidateRemoteObject(memberAutoId);
		MatchCandidate candidate = remoteCandidate.getObject();
		answer.setSubmittedTime(new Date());
		RemoteMatch remoteMatch = getRemoteMatch(candidate.getRemoteMatchKey());
		RemoteObject<MatchCompetitor> remoteCompetitor = remoteMatch.getCompetitorSlot(candidate.getRemoteCompetitorSlot());
		if (remoteCompetitor.tryLock(settings.getMaxLockWaitTime())) {
			boolean competitorDone = false;
			try {
				MatchCompetitor competitor = remoteCompetitor.getObject();
				if (answer.getType().equals(MatchAnswerType.SUBMITTED)) {
					Question q = questionDao.findByKey(remoteMatch.getQuestionIds()[questionOrder - 1]);
					double score = q.getScore(answer.getContent());
					answer.setScore(score);
					answer.setCorrect(score > 0);
				}

				competitor.setTotalScore(competitor.getTotalScore() + answer.getScore());
				List<MatchAnswer> answers = competitor.getAnswers();
				int insertPos = questionOrder - 1;
				if (answers.size() < insertPos) {
					for (int i = answers.size(); i < insertPos; i++)
						competitor.addAnswer(new MatchAnswer(MatchAnswerType.IGNORED, null));
				}
				if (insertPos < answers.size()) {
					answers.set(insertPos, answer);
				} else
					competitor.addAnswer(answer);

				if (competitor.getPassedQuestionCount() == settings.getQuestionPerMatch()) {
					competitor.setFinishedMoment(timeProvider.getCurrentTimeStamp());
					competitorDone = true;
				}
				remoteCompetitor.putObject(competitor);
			} finally {
				remoteCompetitor.unlock();
			}

			if (competitorDone) {
				if (remoteMatch.tryLockMatch()) {
					checkMatchFinished(remoteMatch);
					remoteMatch.unlockMatch();
				}
			}
		} else
			throw new RuntimeException("Cannot acquire competitor lock for competitor#" + candidate.getRemoteCompetitorSlot()
					+ " on match#" + remoteMatch.getKey());
	}

	/**
	 * Competitor submit an answer
	 * 
	 * 
	 * @param userAutoId
	 * @param questionOrder
	 *            Must be in 1-based index
	 * @param answer
	 * @throws TimeoutException
	 */
	public void submitAnswer(Long userAutoId, int questionOrder, String answer) throws TimeoutException {
		initialize();
		addMatchAnswer(userAutoId, questionOrder, new MatchAnswer(MatchAnswerType.SUBMITTED, answer));
	}

	/**
	 * Competitor ignore a question in match
	 * 
	 * @param userAutoId
	 * @param questionOrder
	 *            Must be in 1-based index
	 * @throws TimeoutException
	 */
	public void ignoreQuestion(Long userAutoId, int questionOrder) throws TimeoutException {
		initialize();
		addMatchAnswer(userAutoId, questionOrder, new MatchAnswer(MatchAnswerType.IGNORED, null));
	}

	public void addEventListener(EventListener listener) {
		listeners.add(listener);
	}

	public void resetLocks() {
	}

}
