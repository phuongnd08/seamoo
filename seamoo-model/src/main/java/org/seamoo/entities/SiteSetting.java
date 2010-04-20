package org.seamoo.entities;

import javax.persistence.Id;

import com.vercer.engine.persist.annotation.Key;

public class SiteSetting {
	@Id
	@Key
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
