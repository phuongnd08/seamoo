package org.seamoo.persistence.test.jpa.model;

import com.vercer.engine.persist.annotation.Key;
import com.vercer.engine.persist.annotation.Store;

public class ExampleModel {
	@Key
	private Long autoId;
	private String field;
	@Store(true)
	private transient String transientField;

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

	public void setTransientField(String transientField) {
		this.transientField = transientField;
	}

	public String getTransientField() {
		return transientField;
	}
}
