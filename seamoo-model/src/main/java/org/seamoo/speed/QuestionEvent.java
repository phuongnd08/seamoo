package org.seamoo.speed;

import com.vercer.engine.persist.annotation.Key;

public class QuestionEvent {
	@Key
	private Long autoId;
	private QuestionEventType type;
	private Long questionAutoId;
	private Long leagueAutoId;
	private long timeStamp;

	public QuestionEvent() {
	}

	public QuestionEvent(QuestionEventType type, Long questionAutoId, long timeStamp) {
		this.type = type;
		this.questionAutoId = questionAutoId;
		this.timeStamp = timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setQuestionAutoId(Long questionAutoId) {
		this.questionAutoId = questionAutoId;
	}

	public Long getQuestionAutoId() {
		return questionAutoId;
	}

	public void setType(QuestionEventType type) {
		this.type = type;
	}

	public QuestionEventType getType() {
		return type;
	}

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public Long getAutoId() {
		return autoId;
	}

	public void setLeagueAutoId(Long leagueAutoId) {
		this.leagueAutoId = leagueAutoId;
	}

	public Long getLeagueAutoId() {
		return leagueAutoId;
	}

}
