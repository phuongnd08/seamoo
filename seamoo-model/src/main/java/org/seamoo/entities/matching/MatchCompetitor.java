package org.seamoo.entities.matching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.seamoo.entities.Member;

import com.google.gwt.user.client.rpc.GwtTransient;
import com.vercer.engine.persist.annotation.Embed;
import com.vercer.engine.persist.annotation.Store;

/**
 * Information about a member joining in the match, include member score, rank in the match, the time member join the match
 * 
 * @author phuongnd08
 * 
 */
public class MatchCompetitor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1680471706907193985L;

	private Long memberAutoId;

	private double totalScore;

	private int rank;

	private double additionalAccumulatedScore;

	private long finishedMoment;

	private int passedQuestionCount;

	@GwtTransient
	private List<MatchAnswer> answers;

	public MatchCompetitor() {
		answers = new ArrayList<MatchAnswer>();
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

	public int getCorrectCount() {
		int count = 0;
		if (answers != null)
			for (MatchAnswer a : answers) {
				if (a.getType() == MatchAnswerType.SUBMITTED && a.isCorrect())
					count++;
			}
		return count;
	}

	public int getWrongCount() {
		int count = 0;
		if (answers != null)
			for (MatchAnswer a : answers) {
				if (a.getType() == MatchAnswerType.SUBMITTED && !a.isCorrect())
					count++;
			}
		return count;
	}

	public int getIgnoreCount() {
		int count = 0;
		if (answers != null)
			for (MatchAnswer a : answers) {
				if (a.getType() == MatchAnswerType.IGNORED)
					count++;
			}
		return count;
	}

	@Override
	public String toString() {
		return "Competitor{" + memberAutoId + "}";
	}

	public void setAdditionalAccumulatedScore(double additionalAccumulatedScore) {
		this.additionalAccumulatedScore = additionalAccumulatedScore;
	}

	public double getAdditionalAccumulatedScore() {
		return additionalAccumulatedScore;
	}

	public void setMemberAutoId(Long memberAutoId) {
		this.memberAutoId = memberAutoId;
	}

	public Long getMemberAutoId() {
		return memberAutoId;
	}
}
