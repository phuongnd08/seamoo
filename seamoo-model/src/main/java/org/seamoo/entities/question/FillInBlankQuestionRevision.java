package org.seamoo.entities.question;

public class FillInBlankQuestionRevision extends QuestionRevision {
	private String content;
	private String answer;

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswer() {
		return answer;
	}

	@Override
	public double getScore(String answer) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unsupported");
	}
}
