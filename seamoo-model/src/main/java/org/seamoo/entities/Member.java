package org.seamoo.entities;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Unique;

@PersistenceCapable
public class Member {
	@Unique
	@Persistent
	private String openId;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long autoId;
	@Persistent
	private String displayName;
	@Persistent
	private String email;
	@Persistent
	private Date dateOfBirth;
	@Persistent
	private Date joiningDate;
	@Persistent
	private String aboutMe;
	@Persistent
	private String website;
	@Persistent
	private String quote;
	@Persistent
	private Date lastSeen;
	@Persistent
	private long lockingDuration;
	@Persistent
	private boolean locked;
	@Persistent
	private Date lastLocked;

	public Member() {

	}

	public Member(String openId) {
		this.openId = openId;
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
}
