package org.seamoo.entities;

import java.io.Serializable;

import javax.persistence.Id;

import com.vercer.engine.persist.annotation.Key;

public class League implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9096904769164051434L;
	@Id
	@Key
	private Long autoId;

	private Long subjectAutoId;

	private String name;

	private String alias;

	private String logoUrl;

	private String description;

	private int level;

	private boolean enabled;

	public League() {

	}

	public League(Long autoId, String name, String logoUrl, String description, int level, boolean enabled) {
		this.autoId = autoId;
		this.name = name;
		this.logoUrl = logoUrl;
		this.description = description;
		this.level = level;
		this.enabled = enabled;
	}

	public void setSubjectAutoId(Long subjectAutoId) {
		this.subjectAutoId = subjectAutoId;
	}

	public Long getSubjectAutoId() {
		return subjectAutoId;
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

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public Long getAutoId() {
		return autoId;
	}

	public String getAlias() {
		return alias;
	}
}
