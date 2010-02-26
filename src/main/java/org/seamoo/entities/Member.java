package org.seamoo.entities;

import java.util.Date;

public class Member {
	private String openId;
	private String displayName;
	private String email;
	private Date dateOfBirth;
	private Date joiningDate;
	private String aboutMe;
	private String website;
	private String quote;
	private Date lastSeen;
	private long lockingDuration;
	private boolean locked;
	private Date lastLocked;
	
	
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
}