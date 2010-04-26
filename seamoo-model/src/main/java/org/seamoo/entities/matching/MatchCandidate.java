package org.seamoo.entities.matching;

import java.io.Serializable;

public class MatchCandidate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6934095741107742969L;
	private Long memberAutoId;
	private long lastSeenMoment;
	private String currentMatchUUID;

	public void setMemberAutoId(Long memberAutoId) {
		this.memberAutoId = memberAutoId;
	}

	public Long getMemberAutoId() {
		return memberAutoId;
	}

	public void setCurrentMatchUUID(String currentMatchUUID) {
		this.currentMatchUUID = currentMatchUUID;
	}

	public String getCurrentMatchUUID() {
		return currentMatchUUID;
	}

	public void setLastSeenMoment(long lastSeenMoment) {
		this.lastSeenMoment = lastSeenMoment;
	}

	public long getLastSeenMoment() {
		return lastSeenMoment;
	}
}
