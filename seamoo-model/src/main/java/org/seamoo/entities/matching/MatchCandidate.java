package org.seamoo.entities.matching;

import java.util.Date;

import org.seamoo.entities.League;
import org.seamoo.entities.Member;

public class MatchCandidate {
	private Member member;
	private League league;
	private Date lastRequestTime;

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

	public void setLastRequestTime(Date lastRequestTime) {
		this.lastRequestTime = lastRequestTime;
	}

	public Date getLastRequestTime() {
		return lastRequestTime;
	}
}
