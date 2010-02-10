package org.seamoo.entities.question;

public class ListeningQuestionRevision extends QuestionRevision {

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
}
