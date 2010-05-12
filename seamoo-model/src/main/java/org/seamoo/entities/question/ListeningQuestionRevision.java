package org.seamoo.entities.question;

public class ListeningQuestionRevision extends QuestionRevision {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1627240630356183641L;
	private String audioUrl;
	private String content;

	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}

	public String getAudioUrl() {
		return audioUrl;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	@Override
	public double getScore(String answer) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unsupported");
	}

	@Override
	public String getCorrectAnswer() {
		// TODO Auto-generated method stub
		return content;
	}

	@Override
	public String getTranslatedAnswer(String userAnswer) {
		// TODO Auto-generated method stub
		return userAnswer;
	}
}
