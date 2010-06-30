package org.seamoo.entities.question;

public class FollowPatternQuestionRevision extends QuestionRevision {

	private static final long serialVersionUID = 4983811647766685866L;
	private String content;
	private String pattern;

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

}
