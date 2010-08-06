package org.seamoo.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import com.google.gwt.user.client.rpc.GwtTransient;
import com.vercer.engine.persist.annotation.Key;
import com.vercer.engine.persist.annotation.Store;

public class Member implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9128570735254847132L;
	private static final long MILLS_PER_DAY = 1000 * 60 * 60 * 24;
	private static final long MILLS_PER_YEAR = MILLS_PER_DAY * 365;

	private String openId;
	@Id
	@Key
	private Long autoId;

	private String displayName;

	@Store(true)
	@GwtTransient
	private String email;

	private String emailHash;

	@GwtTransient
	private Date dateOfBirth;
	
	@GwtTransient
	private boolean bot;

	public String getAge() {
		if (dateOfBirth == null)
			return "";
		return String.valueOf((new Date().getTime() - dateOfBirth.getTime()) / MILLS_PER_YEAR);
	}

	@Store(true)
	transient private Date joiningDate;

	@Store(true)
	transient private String aboutMe;

	@Store(true)
	transient private String website;

	private String quote;

	@Store(true)
	transient private Date lastSeen;

	private long lockingDuration;

	private boolean locked;

	private Date lastLocked;

	private String alias;

	@GwtTransient
	private boolean administrator;
	@GwtTransient
	private boolean moderator;
	@GwtTransient
	private double reputationCapacity;
	private double reputation;
	@GwtTransient
	private double spammerScore;

	public Member() {

	}

	public Member(String openId) {
		this.openId = openId;
	}

	public Member(String openId, String displayName, String alias, String emailHash, boolean administrator) {
		this.openId = openId;
		this.displayName = displayName;
		this.alias = alias;
		this.emailHash = emailHash;
		this.administrator = administrator;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		if (displayName == null || displayName.length() == 0)
			return "(unknown)";
		return displayName;
	}

	public String getRealDisplayName() {
		return displayName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getWebsite() {
		return website;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getQuote() {
		return quote;
	}

	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}

	public Date getLastSeen() {
		return lastSeen;
	}

	public void setLockingDuration(long lockingDuration) {
		this.lockingDuration = lockingDuration;
	}

	public long getLockingDuration() {
		return lockingDuration;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean getLocked() {
		return locked;
	}

	public void setLastLocked(Date lastLocked) {
		this.lastLocked = lastLocked;
	}

	public Date getLastLocked() {
		return lastLocked;
	}

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public Long getAutoId() {
		return autoId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setEmailHash(String emailHash) {
		this.emailHash = emailHash;
	}

	public String getEmailHash() {
		return emailHash;
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

	public void setBot(boolean bot) {
		this.bot = bot;
	}

	public boolean isBot() {
		return bot;
	}
}
