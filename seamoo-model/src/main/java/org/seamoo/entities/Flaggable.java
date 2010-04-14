package org.seamoo.entities;

import java.io.Serializable;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Flaggable implements Serializable {
	/**
	 * 
	 */
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long autoId;
	private static final long serialVersionUID = 6618772456611898530L;
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

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public Long getAutoId() {
		return autoId;
	}
}
