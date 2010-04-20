package org.seamoo.entities.question;

import java.io.Serializable;

public class QuestionChoice implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -563242659588171862L;
	private String content;
	private boolean correct;

	public QuestionChoice() {
	}

	public QuestionChoice(String content, boolean correct) {
		this.content = content;
		this.correct = correct;
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
}
