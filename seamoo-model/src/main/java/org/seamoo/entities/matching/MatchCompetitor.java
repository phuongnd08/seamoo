package org.seamoo.entities.matching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seamoo.entities.Member;

/**
 * Information about a member joining in the match, include member score, rank
 * in the match, the time member join the match
 * 
 * @author phuongnd08
 * 
 */
public class MatchCompetitor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1680471706907193985L;

	private Member member;

	private Date participatedTime;

	private double totalScore;

	private int rank;

	private long finishedMoment;

	private int passedQuestionCount;

	private List<MatchAnswer> answers;

	public MatchCompetitor() {
		answers = new ArrayList<MatchAnswer>();
	}

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

	public void setPassedQuestionCount(int passedQuestionCount) {
		this.passedQuestionCount = passedQuestionCount;
	}

	public int getPassedQuestionCount() {
		return passedQuestionCount;
	}

	public void addAnswer(MatchAnswer answer) {
		answers.add(answer);
		totalScore += answer.getScore();
		passedQuestionCount++;
	}

	public List<MatchAnswer> getAnswers() {
		return answers;
	}

	public void setFinishedMoment(long finishedMoment) {
		this.finishedMoment = finishedMoment;
	}

	public long getFinishedMoment() {
		return finishedMoment;
	}

	public String getAlias() {
		if (member == null)
			return null;
		return member.getDisplayName();
	}

	public int getCorrectCount() {
		int count = 0;
		for (MatchAnswer a : answers) {
			if (a != null)
				if (a.getType() == MatchAnswerType.SUBMITTED && a.isCorrect())
					count++;
		}
		return count;
	}

	public int getWrongCount() {
		int count = 0;
		for (MatchAnswer a : answers) {
			if (a != null)
				if (a.getType() == MatchAnswerType.SUBMITTED && !a.isCorrect())
					count++;
		}
		return count;
	}

	public int getIgnoreCount() {
		int count = 0;
		for (MatchAnswer a : answers) {
			if (a != null)
				if (a.getType() == MatchAnswerType.IGNORED)
					count++;
		}
		return count;
	}
}
