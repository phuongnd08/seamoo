package org.seamoo.entities.matching;

import java.util.Date;

import org.seamoo.entities.League;
import org.seamoo.entities.Member;

public class MatchCandidate {
	private Member member;
	private League league;
	private long lastSeenMoment;
	private Match currentMatch;

	public void setMember(Member member) {
		this.member = member;
	}

	public Member getMember() {
		return member;
	}

	public void setLeague(League league) {
		this.league = league;
	}

	public League getLeague() {
		return league;
	}

	public void setCurrentMatch(Match currentMatch) {
		this.currentMatch = currentMatch;
	}

	public Match getCurrentMatch() {
		return currentMatch;
	}

	public void setLastSeenMoment(long lastSeenMoment) {
		this.lastSeenMoment = lastSeenMoment;
	}

	public long getLastSeenMoment() {
		return lastSeenMoment;
	}
}
