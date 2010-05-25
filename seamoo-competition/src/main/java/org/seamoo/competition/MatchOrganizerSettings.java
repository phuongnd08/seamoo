package org.seamoo.competition;

public class MatchOrganizerSettings {
	public static final long CANDIDATE_ACTIVE_PERIOD = 10000;// 5 seconds
	public static final long MATCH_COUNTDOWN_TIME = 10000;
	public static final int MAX_CANDIDATE_PER_MATCH = 4;
	public static final int MIN_CANDIDATE_PER_MATCH = 2;
	public static final int QUESTION_PER_MATCH = 20;
	public static final long MATCH_TIME = 120000;// 2 minutes
	public static final long MAX_LOCK_WAIT_TIME = 5000;
	public static final long CHECK_MATCH_INTERVAL=100;
	public static final long MAX_CHECK_MATCH_PERIOD=3000;

	public MatchOrganizerSettings() {
		this.candidateActivePeriod = CANDIDATE_ACTIVE_PERIOD;
		this.matchCountDownTime = MATCH_COUNTDOWN_TIME;
		this.setMaxCandidatePerMatch(MAX_CANDIDATE_PER_MATCH);
		this.setMinCandidatePerMatch(MIN_CANDIDATE_PER_MATCH);
		this.questionPerMatch = QUESTION_PER_MATCH;
		this.matchTime = MATCH_TIME;
		this.maxLockWaitTime = MAX_LOCK_WAIT_TIME;
		this.setCheckMatchInterval(CHECK_MATCH_INTERVAL);
		this.setMaxCheckMatchPeriod(MAX_CHECK_MATCH_PERIOD);
	}

	private long candidateActivePeriod;
	private long matchCountDownTime;
	private int maxCandidatePerMatch;
	private int minCandidatePerMatch;
	private int questionPerMatch;
	private long matchTime;
	private long maxLockWaitTime;
	private long checkMatchInterval;
	private long maxCheckMatchPeriod;

	public void setCandidateActivePeriod(long candidateActivePeriod) {
		this.candidateActivePeriod = candidateActivePeriod;
	}

	public long getCandidateActivePeriod() {
		return candidateActivePeriod;
	}

	public void setMatchCountDownTime(long matchCountDownTime) {
		this.matchCountDownTime = matchCountDownTime;
	}

	public long getMatchCountDownTime() {
		return matchCountDownTime;
	}

	public void setQuestionPerMatch(int questionPerMatch) {
		this.questionPerMatch = questionPerMatch;
	}

	public int getQuestionPerMatch() {
		return questionPerMatch;
	}

	public void setMatchTime(long matchTime) {
		this.matchTime = matchTime;
	}

	public long getMatchTime() {
		return matchTime;
	}

	public void setMaxLockWaitTime(long maxLockWaitTime) {
		this.maxLockWaitTime = maxLockWaitTime;
	}

	public long getMaxLockWaitTime() {
		return maxLockWaitTime;
	}

	public void setCheckMatchInterval(long checkMatchInterval) {
		this.checkMatchInterval = checkMatchInterval;
	}

	public long getCheckMatchInterval() {
		return checkMatchInterval;
	}

	public void setMaxCheckMatchPeriod(long maxCheckMatchPeriod) {
		this.maxCheckMatchPeriod = maxCheckMatchPeriod;
	}

	public long getMaxCheckMatchPeriod() {
		return maxCheckMatchPeriod;
	}

	public void setMaxCandidatePerMatch(int maxCandidatePerMatch) {
		this.maxCandidatePerMatch = maxCandidatePerMatch;
	}

	public int getMaxCandidatePerMatch() {
		return maxCandidatePerMatch;
	}

	public void setMinCandidatePerMatch(int minCandidatePerMatch) {
		this.minCandidatePerMatch = minCandidatePerMatch;
	}

	public int getMinCandidatePerMatch() {
		return minCandidatePerMatch;
	}
}
