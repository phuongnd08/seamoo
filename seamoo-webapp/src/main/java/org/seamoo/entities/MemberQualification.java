package org.seamoo.entities;

public class MemberQualification {
	private Member member;
	private double reputationCapacity;
	private double reputation;
	private double spammerScore;
	private int level;
	private boolean administrator;
	private boolean moderator;
	
	public void setMember(Member member) {
		this.member = member;
	}
	public Member getMember() {
		return member;
	}
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
}
