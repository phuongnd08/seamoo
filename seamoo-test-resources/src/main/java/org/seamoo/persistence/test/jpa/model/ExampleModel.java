package org.seamoo.persistence.test.jpa.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExampleModel {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long autoId;
	private String field;
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
