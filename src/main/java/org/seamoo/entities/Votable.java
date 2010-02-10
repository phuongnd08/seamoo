package org.seamoo.entities;

import java.util.List;

public class Votable extends Praisable{
	private List<Member> downvoters;

	public void setDownvoters(List<Member> downvoters) {
		this.downvoters = downvoters;
	}

	public List<Member> getDownvoters() {
		return downvoters;
	}
}
