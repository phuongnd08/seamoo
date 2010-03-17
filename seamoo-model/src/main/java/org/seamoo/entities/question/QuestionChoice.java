package org.seamoo.entities.question;

public class QuestionChoice {
	private String content;
	private boolean correct;

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
