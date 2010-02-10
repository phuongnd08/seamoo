package org.seamoo.entities.question;

public class FillInBlankQuestionRevision extends QuestionRevision{
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
}
