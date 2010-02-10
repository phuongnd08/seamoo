package org.seamoo.entities.question;

public class Tag {
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
