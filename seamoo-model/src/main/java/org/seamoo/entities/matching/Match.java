package org.seamoo.entities.matching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

import org.seamoo.entities.question.Question;

import com.vercer.engine.persist.annotation.Embed;
import com.vercer.engine.persist.annotation.Key;
import com.vercer.engine.persist.annotation.Store;

public class Match implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1901172087193249663L;

	@Id
	@Key
	private Long autoId;

	private long formedMoment;

	private long startedMoment;

	private long endedMoment;

	private List<Long> questionIds;

	@Embed
	private List<MatchCompetitor> competitors;

	private List<Long> memberAutoIds;

	private MatchPhase phase;

	private Long leagueAutoId;

	private String alias;

	@Store(false)
	private String description;

	public Match() {
		competitors = new ArrayList<MatchCompetitor>();
		memberAutoIds = new ArrayList<Long>();
	}

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public Long getAutoId() {
		return autoId;
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
		memberAutoIds.add(competitor.getMemberAutoId());
	}

	public void removeCompetitor(MatchCompetitor competitor) {
		memberAutoIds.remove(competitor.getMemberAutoId());
		this.competitors.remove(competitor);
	}

	public List<MatchCompetitor> getCompetitors() {
		return competitors;
	}

	public MatchCompetitor getCompetitorForMember(Long memberAutoId) {
		for (MatchCompetitor competitor : competitors)
			if (competitor.getMemberAutoId().equals(memberAutoId))
				return competitor;
		return null;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setLeagueAutoId(Long leagueAutoId) {
		this.leagueAutoId = leagueAutoId;
	}

	public Long getLeagueAutoId() {
		return leagueAutoId;
	}

	public List<Long> getMemberAutoIds() {
		return memberAutoIds;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}

	public List<Long> getQuestionIds() {
		return questionIds;
	}
}
