package org.seamoo.entities;

import java.util.List;

public class Votable extends Praisable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4660898080172025336L;
	private List<Member> downvoters;

	public void setDownvoters(List<Member> downvoters) {
		this.downvoters = downvoters;
	}

	public List<Member> getDownvoters() {
		return downvoters;
	}
}
