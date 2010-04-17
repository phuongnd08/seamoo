package org.seamoo.entities.matching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

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
	@Persistent
	private Member member;
	@Persistent
	private Date participatedTime;
	@Persistent
	private double totalScore;
	@Persistent
	private int rank;
	@Persistent
	private long finishedMoment;
	@Persistent
	private int passedQuestionCount;
	@Persistent
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

	public void addAnswer(int position, MatchAnswer answer) {
		if (position == answers.size()) {
			answers.add(answer);
			totalScore += answer.getScore();
		} else if (position < answers.size()) {
			if (answers.get(position) != null)
				throw new RuntimeException("Answer for the same question submitted twice. Ignored");
			answers.set(position, answer);
			totalScore += answer.getScore();
		} else /* position>answers.size() */{
			for (int i = answers.size(); i < position; i++)
				answers.add(null);
			answers.add(answer);
			totalScore += answer.getScore();
		}
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
}
