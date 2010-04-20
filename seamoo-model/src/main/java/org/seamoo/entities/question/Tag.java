package org.seamoo.entities.question;

import java.io.Serializable;

import javax.persistence.Id;

import com.vercer.engine.persist.annotation.Key;

public class Tag implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4923080074856815753L;
	@Id
	@Key
	private String name;
	private long count;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getCount() {
		return count;
	}
}
