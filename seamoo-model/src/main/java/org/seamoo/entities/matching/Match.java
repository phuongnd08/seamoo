package org.seamoo.entities.matching;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

import org.seamoo.entities.question.Question;

@PersistenceCapable
public class Match {
	@Id
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long autoId;
	@Persistent
	private long formedMoment;
	@Persistent
	private long startedMoment;
	@Persistent
	private long endedMoment;
	@Persistent
	private List<Question> questions;
	@Persistent
	private List<MatchCompetitor> competitors;
	@Persistent
	private List<MatchEvent> events;
	@Persistent
	private MatchPhase phase;

	public Match() {
		competitors = new ArrayList<MatchCompetitor>();
		events = new ArrayList<MatchEvent>();
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

	public void setPhase(MatchPhase phase) {
		this.phase = phase;
	}

	public MatchPhase getPhase() {
		return phase;
	}

	public void setFormedMoment(long formedMoment) {
		this.formedMoment = formedMoment;
	}

	public long getFormedMoment() {
		return formedMoment;
	}

	public void setStartedMoment(long startedMoment) {
		this.startedMoment = startedMoment;
	}

	public long getStartedMoment() {
		return startedMoment;
	}

	public void setEndedMoment(long endedMoment) {
		this.endedMoment = endedMoment;
	}

	public long getEndedMoment() {
		return endedMoment;
	}

	public void addCompetitor(MatchCompetitor competitor) {
		this.competitors.add(competitor);
	}

	public void removeCompetitor(MatchCompetitor competitor) {
		this.competitors.remove(competitor);
	}

	public List<MatchCompetitor> getCompetitors() {
		return competitors;
	}

	public void addEvent(MatchEvent event) {
		this.events.add(event);
	}

	public List<MatchEvent> getEvents() {
		return events;
	}
}
