package org.seamoo.entities;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable = "false")
public class Subject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3398469031026720030L;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long autoId;
	@Persistent
	private String name;
	@Persistent
	private String logoUrl;
	@Persistent
	private String description;
	@Persistent
	private boolean enabled;
	@Persistent
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "@Subject{" + name + "}";
	}
}
