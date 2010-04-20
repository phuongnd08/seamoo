package org.seamoo.entities;

import java.util.List;

public class Praisable extends Flaggable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5524569032274799368L;
	private int votingScore;
	private List<Member> upvoters;

	public void setVotingScore(int votingScore) {
		this.votingScore = votingScore;
	}

	public int getVotingScore() {
		return votingScore;
	}

	public void setUpvoters(List<Member> upvoters) {
		this.upvoters = upvoters;
	}

	public List<Member> getUpvoters() {
		return upvoters;
	}
}
