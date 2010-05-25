package org.seamoo.competition;

import java.util.ArrayList;
import java.util.List;

import org.seamoo.entities.matching.MatchPhase;

public class TemporalMatch {
	private List<Long> competitorIds;
	private int competitorCount;
	private List<Long> questionIds;
	private long readyMoment;

	public TemporalMatch() {
		competitorIds = new ArrayList<Long>();
		questionIds = new ArrayList<Long>();
		competitorCount = 0;
	}

	public void addCompetitor(Long memberAutoId) {
		competitorIds.add(memberAutoId);
	}

	public List<Long> getCompetitorIds() {
		return competitorIds;
	}

	public void setCompetitorCount(int competitorCount) {
		this.competitorCount = competitorCount;
	}

	public int getCompetitorCount() {
		return competitorCount;
	}

	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}

	public List<Long> getQuestionIds() {
		return questionIds;
	}
}
