package org.seamoo.entities;

import java.util.List;

public class Subject {
	private String name;
	private String avatarUrl;
	private String description;
	private boolean enabled;
	private List<League> leagues;

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

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setLeagues(List<League> leagues) {
		this.leagues = leagues;
	}

	public List<League> getLeagues() {
		return leagues;
	}
}
