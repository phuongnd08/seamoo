package org.seamoo.entities;

import javax.jdo.annotations.Unique;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SiteSetting {
	@Id
	@Unique
	private String key;
	public String value;

	public SiteSetting() {
	}

	public SiteSetting(String key, String value) {
		this();
		this.key = key;
		this.value = value;

	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
