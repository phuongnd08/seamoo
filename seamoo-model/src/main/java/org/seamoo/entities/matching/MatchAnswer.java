package org.seamoo.entities.matching;

import java.io.Serializable;
import java.util.Date;

public class MatchAnswer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 116932736180099741L;
	private MatchAnswerType type;

	private String content;

	private boolean correct;

	private double score;
	
	private Date submittedTime;

	public MatchAnswer() {
	}

	public MatchAnswer(MatchAnswerType type, String content) {
		this.type = type;
		this.setContent(content);
	}

	public void setType(MatchAnswerType type) {
		this.type = type;
	}

	public MatchAnswerType getType() {
		return type;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getScore() {
		return score;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setSubmittedTime(Date submittedTime) {
		this.submittedTime = submittedTime;
	}

	public Date getSubmittedTime() {
		return submittedTime;
	}
}
