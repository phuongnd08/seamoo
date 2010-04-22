package org.seamoo.entities.matching;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import org.seamoo.entities.Member;
import org.seamoo.entities.question.Question;

import com.vercer.engine.persist.annotation.Key;

public class Match {
	@Id
	@Key
	private Long autoId;

	private long formedMoment;

	private long startedMoment;

	private long endedMoment;

	private List<Question> questions;

	private List<MatchCompetitor> competitors;

	private List<MatchEvent> events;

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

	public MatchCompetitor getCompetitorForMember(Member member) {
		for (MatchCompetitor competitor : competitors)
			if (competitor.getMember().getAutoId() == member.getAutoId())
				return competitor;
		return null;
	}

	public void addEvent(MatchEvent event) {
		this.events.add(event);
	}

	public List<MatchEvent> getEvents() {
		return events;
	}

	public String getAlias() {
		String alias = "";
		for (int i = 0; i < competitors.size(); i++) {
			alias += competitors.get(i).getAlias();
			if (i < competitors.size() - 1)
				alias += "-";
		}
		return alias;
	}
}
