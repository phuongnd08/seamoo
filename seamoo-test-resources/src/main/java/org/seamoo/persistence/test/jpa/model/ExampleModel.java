package org.seamoo.persistence.test.jpa.model;

import javax.persistence.Id;

import com.vercer.engine.persist.annotation.Key;

public class ExampleModel {
	@Id
	@Key
	Long autoId;
	String field;

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public long getAutoId() {
		return autoId;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}
}
