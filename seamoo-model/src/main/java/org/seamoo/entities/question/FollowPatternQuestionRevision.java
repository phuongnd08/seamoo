package org.seamoo.entities.question;

import com.google.gwt.user.client.rpc.GwtTransient;

public class FollowPatternQuestionRevision extends QuestionRevision {

	private static final long serialVersionUID = 4983811647766685866L;
	private String content;
	@GwtTransient
	private String pattern;
	private String guidingPattern;

	@Override
	public String getCorrectAnswer() {
		return pattern.replace("[", "").replace("]", "");
	}

	@Override
	public double getScore(String answer) {
		if (getCorrectAnswer().equalsIgnoreCase(answer))
			return 1.0;
		return 0.0;
	}

	@Override
	public String getTranslatedAnswer(String userAnswer) {
		return userAnswer;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setGuidingPattern(String guidingPattern) {
		this.guidingPattern = guidingPattern;
	}

	public String getGuidingPattern() {
		return guidingPattern;
	}

}
