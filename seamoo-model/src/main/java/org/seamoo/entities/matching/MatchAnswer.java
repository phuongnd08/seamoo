package org.seamoo.entities.matching;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class MatchAnswer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 116932736180099741L;
	private MatchAnswerType type;
	private String answer;
	private double score;
	public void setType(MatchAnswerType type) {
		this.type = type;
	}
	public MatchAnswerType getType() {
		return type;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswer() {
		return answer;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getScore() {
		return score;
	}
}
