package org.seamoo.entities.matching;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;

import org.seamoo.entities.Member;

/**
 * Information about a member joining in the match, include member score, rank
 * in the match, the time member join the match
 * 
 * @author phuongnd08
 * 
 */
@PersistenceCapable
public class MatchCompetitor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1680471706907193985L;
	private Member member;
	private Date participatedTime;
	private double totalScore;
	private int rank;
	private Date endTime;
	private int passedQuestionCount;

	public void setParticipatedTime(Date participatedTime) {
		this.participatedTime = participatedTime;
	}

	public Date getParticipatedTime() {
		return participatedTime;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getMember() {
		return member;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setPassedQuestionCount(int passedQuestionCount) {
		this.passedQuestionCount = passedQuestionCount;
	}

	public int getPassedQuestionCount() {
		return passedQuestionCount;
	}
}
