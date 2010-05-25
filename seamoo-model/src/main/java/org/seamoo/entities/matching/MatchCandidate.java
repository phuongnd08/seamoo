package org.seamoo.entities.matching;

import java.io.Serializable;

public class MatchCandidate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6934095741107742969L;
	private Long memberAutoId;
	private Long remoteMatchKey;
	private int remoteCompetitorSlot;
	public void setMemberAutoId(Long memberAutoId) {
		this.memberAutoId = memberAutoId;
	}
	public Long getMemberAutoId() {
		return memberAutoId;
	}
	public void setRemoteMatchKey(Long remoteMatchKey) {
		this.remoteMatchKey = remoteMatchKey;
	}
	public Long getRemoteMatchKey() {
		return remoteMatchKey;
	}
	public void setRemoteCompetitorSlot(int remoteCompetitorSlot) {
		this.remoteCompetitorSlot = remoteCompetitorSlot;
	}
	public int getRemoteCompetitorSlot() {
		return remoteCompetitorSlot;
	}
}
