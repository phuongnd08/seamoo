package org.seamoo.entities;

import com.vercer.engine.persist.annotation.Key;

public class LeagueMembership {
	@Key
	private Long autoId;
	private Long memberAutoId;
	private Long leagueAutoId;
	private int month;
	private int year;
	private double accumulatedScore;
	private LeagueResult result = LeagueResult.UNDETERMINED;
	private boolean resultCalculated = false;
	private int matchCount;

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public Long getAutoId() {
		return autoId;
	}

	public void setMemberAutoId(Long memberAutoId) {
		this.memberAutoId = memberAutoId;
	}

	public Long getMemberAutoId() {
		return memberAutoId;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMonth() {
		return month;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public void setResult(LeagueResult result) {
		this.result = result;
	}

	public LeagueResult getResult() {
		return result;
	}

	public void setLeagueAutoId(Long leagueAutoId) {
		this.leagueAutoId = leagueAutoId;
	}

	public Long getLeagueAutoId() {
		return leagueAutoId;
	}

	public void setAccumulatedScore(double accumulatedScore) {
		this.accumulatedScore = accumulatedScore;
	}

	public double getAccumulatedScore() {
		return accumulatedScore;
	}

	public void setResultCalculated(boolean resultCalculated) {
		this.resultCalculated = resultCalculated;
	}

	public boolean isResultCalculated() {
		return resultCalculated;
	}

	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	public int getMatchCount() {
		return matchCount;
	}
}
