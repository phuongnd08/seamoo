package org.seamoo.entities.matching;

import java.io.Serializable;

public class MatchAnswer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 116932736180099741L;
	private MatchAnswerType type;

	private String content;

	private double score;

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
}
