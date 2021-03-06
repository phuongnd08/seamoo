package org.seamoo.competition;

import java.util.HashMap;
import java.util.Map;

public class LeagueOrganizerSettings {
	public final static int DEFAULT_MIN_MATCH_SCORE_FOR_ACCUMULATION = 14;
	public final static int DEFAULT_MAX_SCORE_PER_MATCH = 4;
	public final static double DEFAULT_GIVE_UP_DEDUCTION_RATIO = 0.5;
	public final static int DEFAULT_CHECK_BATCH_SIZE = 10;
	public final static double DEFAULT_SCORE_FOR_UP_RELEGATE = 300.0;
	public final static double DEFAULT_SCORE_FOR_STAY = 100.0;

	/**
	 * The minimum number of score/match that a competitor must obtain in order
	 * to accumulate match
	 */
	private int minMatchScoreForAccumulation;
	/**
	 * The maximum number that a competitor can accumulate, will be multiplied
	 * with different ratio depends on competitor ranking
	 */
	private int maxScorePerMatch;
	/**
	 * The number of LeagueMembership to be processed in batch
	 */
	private int checkBatchSize;
	private double scoreForUpRelegate;
	private double scoreForStay;
	private Map<Integer, Double> ratioMap;

	public double getRankRatio(int rank) {
		Double r = ratioMap.get(rank);
		if (r == null)
			return 0;
		return r;
	}

	public void setRankRatio(int rank, double ratio) {
		ratioMap.put(rank, ratio);
	}

	public LeagueOrganizerSettings() {
		this.setMinMatchScoreForAccumulation(DEFAULT_MIN_MATCH_SCORE_FOR_ACCUMULATION);
		this.setMaxScorePerMatch(DEFAULT_MAX_SCORE_PER_MATCH);
		this.setCheckBatchSize(DEFAULT_CHECK_BATCH_SIZE);
		this.setScoreForStay(DEFAULT_SCORE_FOR_STAY);
		this.setScoreForUpRelegate(DEFAULT_SCORE_FOR_UP_RELEGATE);
		this.ratioMap = new HashMap<Integer, Double>();
		this.ratioMap.put(1, 1.0);
		this.ratioMap.put(2, 0.5);
		this.ratioMap.put(3, 0.25);
		this.ratioMap.put(4, 0.125);
	}

	public void setMinMatchScoreForAccumulation(int minMatchScoreForAccumulation) {
		this.minMatchScoreForAccumulation = minMatchScoreForAccumulation;
	}

	public int getMinMatchScoreForAccumulation() {
		return minMatchScoreForAccumulation;
	}

	public void setMaxScorePerMatch(int maxScorePerMatch) {
		this.maxScorePerMatch = maxScorePerMatch;
	}

	public int getMaxScorePerMatch() {
		return maxScorePerMatch;
	}

	public void setCheckBatchSize(int checkBatchSize) {
		this.checkBatchSize = checkBatchSize;
	}

	public int getCheckBatchSize() {
		return checkBatchSize;
	}

	public void setScoreForUpRelegate(double scoreForUpRelegate) {
		this.scoreForUpRelegate = scoreForUpRelegate;
	}

	public double getScoreForUpRelegate() {
		return scoreForUpRelegate;
	}

	public void setScoreForStay(double scoreForStay) {
		this.scoreForStay = scoreForStay;
	}

	public double getScoreForStay() {
		return scoreForStay;
	}
}
