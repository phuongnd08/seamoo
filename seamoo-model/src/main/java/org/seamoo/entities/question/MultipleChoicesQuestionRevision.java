package org.seamoo.entities.question;

import java.util.List;

public class MultipleChoicesQuestionRevision extends QuestionRevision{
	private String content;
	private List<QuestionChoice> choices;
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setChoices(List<QuestionChoice> choices) {
		this.choices = choices;
	}
	public List<QuestionChoice> getChoices() {
		return choices;
	}
}
