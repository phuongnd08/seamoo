package org.seamoo.entities.question;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoicesQuestionRevision extends QuestionRevision {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3124942569355863301L;
	private String content;
	private List<QuestionChoice> choices;

	public MultipleChoicesQuestionRevision() {
		this.choices = new ArrayList<QuestionChoice>();
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void addChoice(QuestionChoice choice) {
		this.choices.add(choice);
	}

	public void clearChoices() {
		this.choices.clear();
	}

	public List<QuestionChoice> getChoices() {
		return choices;
	}

	@Override
	public double getScore(String answer) {
		// TODO Auto-generated method stub
		int choice = Integer.parseInt(answer);
		if (this.choices.get(choice - 1).isCorrect()) {
			return 1.0;
		} else
			return -0.5;
	}

	@Override
	public String getCorrectAnswer() {
		// TODO Auto-generated method stub
		for (int i = 0; i < choices.size(); i++)
			if (choices.get(i).isCorrect())
				return choices.get(i).getContent();
		return "";
	}

	@Override
	public String getTranslatedAnswer(String userAnswer) {
		// TODO Auto-generated method stub
		int i = Integer.parseInt(userAnswer) - 1;
		if (i >= 0 && i < choices.size())
			return choices.get(i).getContent();
		return "";
	}

}
