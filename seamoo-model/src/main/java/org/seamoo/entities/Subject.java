package org.seamoo.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;

import com.vercer.engine.persist.annotation.Key;

public class Subject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3398469031026720030L;
	@Id
	@Key
	private Long autoId;

	private String name;

	private String alias;

	private String logoUrl;

	private String description;

	private boolean enabled;

	private Date addedTime;

	// private List<League> leagues;

	public Subject() {

	}

	public Subject(Long autoId, String name, String avatarUrl, String description, boolean enabled) {
		this.autoId = autoId;
		this.name = name;
		this.logoUrl = avatarUrl;
		this.description = description;
		this.enabled = enabled;
	}

	public void setName(String name) {
		this.name = name;
		this.alias = name.toLowerCase().replace(' ', '-');
	}

	public String getName() {
		return name;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getLogoUrl() {
		return logoUrl;
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

	public String getAlias() {
		return alias;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "@Subject{" + name + "}";
	}
}
