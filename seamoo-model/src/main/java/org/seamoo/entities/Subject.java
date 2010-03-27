package org.seamoo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long autoId;
	private String name;
	private String avatarUrl;
	private String description;
	private boolean enabled;
	// @Temporal(TemporalType.TIMESTAMP)
	private Date addedTime;
	private List<League> leagues;

	public Subject() {

	}

	public Subject(Long autoId, String name, String avatarUrl,
			String description, boolean enabled) {
		this.autoId = autoId;
		this.name = name;
		this.avatarUrl = avatarUrl;
		this.description = description;
		this.enabled = enabled;
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

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public Long getAutoId() {
		return autoId;
	}

	public void setAddedTime(Date addedTime) {
		this.addedTime = addedTime;
	}

	public Date getAddedTime() {
		return addedTime;
	}
}
