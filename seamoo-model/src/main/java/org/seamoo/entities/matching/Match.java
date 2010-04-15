package org.seamoo.entities.matching;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.seamoo.entities.question.Question;

@PersistenceCapable
public class Match {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long autoId;
	@Persistent
	private Date formedTime;
	@Persistent
	private Date startedTime;
	@Persistent
	private Date endedTime;
	@Persistent
	private List<Question> questions;

	public void setFormedTime(Date formedTime) {
		this.formedTime = formedTime;
	}

	public Date getFormedTime() {
		return formedTime;
	}

	public void setStartedTime(Date startedTime) {
		this.startedTime = startedTime;
	}

	public Date getStartedTime() {
		return startedTime;
	}

	public void setEndedTime(Date endedTime) {
		this.endedTime = endedTime;
	}

	public Date getEndedTime() {
		return endedTime;
	}

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public Long getAutoId() {
		return autoId;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<Question> getQuestions() {
		return questions;
	}
}
