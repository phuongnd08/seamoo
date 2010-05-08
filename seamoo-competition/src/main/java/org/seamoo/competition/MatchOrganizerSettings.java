package org.seamoo.competition;

public class MatchOrganizerSettings {
	public static final long CANDIDATE_ACTIVE_PERIOD = 6000;// 6 seconds
	public static final long MATCH_COUNTDOWN_TIME = 10000;
	public static final long MAX_CANDIDATE_PER_MATCH = 4;
	public static final long MIN_CANDIDATE_PER_MATCH = 2;
	public static final int QUESTION_PER_MATCH = 20;
	public static final long MATCH_TIME = 120000;// 2 minutes
	public static final long MAX_LOCK_WAIT_TIME = 5000;

	public MatchOrganizerSettings() {
		this.candidateActivePeriod = CANDIDATE_ACTIVE_PERIOD;
		this.matchCountDownTime = MATCH_COUNTDOWN_TIME;
		this.maxCandidatePerMatch = MAX_CANDIDATE_PER_MATCH;
		this.minCandidatePerMatch = MIN_CANDIDATE_PER_MATCH;
		this.questionPerMatch = QUESTION_PER_MATCH;
		this.matchTime = MATCH_TIME;
		this.maxLockWaitTime = MAX_LOCK_WAIT_TIME;
	}

	private long candidateActivePeriod;
	private long matchCountDownTime;
	private long maxCandidatePerMatch;
	private long minCandidatePerMatch;
	private int questionPerMatch;
	private long matchTime;
	private long maxLockWaitTime;

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

	public void setMaxCandidatePerMatch(long maxCandidatePerMatch) {
		this.maxCandidatePerMatch = maxCandidatePerMatch;
	}

	public long getMaxCandidatePerMatch() {
		return maxCandidatePerMatch;
	}

	public void setMinCandidatePerMatch(long minCandidatePerMatch) {
		this.minCandidatePerMatch = minCandidatePerMatch;
	}

	public long getMinCandidatePerMatch() {
		return minCandidatePerMatch;
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
}
