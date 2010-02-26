package org.seamoo.entities;

import java.util.List;

public class Flaggable {
	private List<Member> flaggers;
	private int spamScore;
	private int duplicateScore;
	public void setFlaggers(List<Member> flaggers) {
		this.flaggers = flaggers;
	}
	public List<Member> getFlaggers() {
		return flaggers;
	}
	public void setSpamScore(int spamScore) {
		this.spamScore = spamScore;
	}
	public int getSpamScore() {
		return spamScore;
	}
	public void setDuplicateScore(int duplicateScore) {
		this.duplicateScore = duplicateScore;
	}
	public int getDuplicateScore() {
		return duplicateScore;
	}
}