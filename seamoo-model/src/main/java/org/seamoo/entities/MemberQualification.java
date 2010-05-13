package org.seamoo.entities;

import com.vercer.engine.persist.annotation.Key;

public class MemberQualification {
	@Key
	private Long autoId;
	private Long memberAutoId;
	private double reputationCapacity;
	private double reputation;
	private double spammerScore;
	private int level;
	private boolean administrator;
	private boolean moderator;
	
	public void setReputationCapacity(double reputationCapacity) {
		this.reputationCapacity = reputationCapacity;
	}
	public double getReputationCapacity() {
		return reputationCapacity;
	}
	public void setReputation(double reputation) {
		this.reputation = reputation;
	}
	public double getReputation() {
		return reputation;
	}
	public void setSpammerScore(double spammerScore) {
		this.spammerScore = spammerScore;
	}
	public double getSpammerScore() {
		return spammerScore;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getLevel() {
		return level;
	}
	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}
	public boolean isAdministrator() {
		return administrator;
	}
	public void setModerator(boolean moderator) {
		this.moderator = moderator;
	}
	public boolean isModerator() {
		return moderator;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}
	public Long getAutoId() {
		return autoId;
	}
	public void setMemberAutoId(Long memberAutoId) {
		this.memberAutoId = memberAutoId;
	}
	public Long getMemberAutoId() {
		return memberAutoId;
	}
}
