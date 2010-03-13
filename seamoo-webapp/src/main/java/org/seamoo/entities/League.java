package org.seamoo.entities;

public class League {
	private Subject subject;
	private String name;
	private String avatarUrl;
	private String description;
	private int level;
	private boolean enabled;
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getLevel() {
		return level;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isEnabled() {
		return enabled;
	}
}
