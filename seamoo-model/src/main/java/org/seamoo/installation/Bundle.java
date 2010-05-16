package org.seamoo.installation;

import com.vercer.engine.persist.annotation.Key;

/**
 * Represent a resource file that used to install data into the site
 * 
 * @author phuongnd08
 * 
 */
public class Bundle {
	@Key
	private String name;
	/**
	 * The number of line inside package that has been processed
	 */
	private Long finished;
	private boolean installed;

	public Bundle() {

	}

	public Bundle(String name, Long finished, boolean installed) {
		this.name = name;
		this.finished = finished;
		this.installed = installed;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

	public boolean isInstalled() {
		return installed;
	}

	public void setFinished(Long finished) {
		this.finished = finished;
	}

	public Long getFinished() {
		return finished;
	}
}
