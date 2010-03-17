package org.seamoo.entities.training;

public class TrainingTag {
	private String name;
	private long count;
	private boolean inUse;
	private TrainingBook book;
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
	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
	public boolean isInUse() {
		return inUse;
	}
	public void setBook(TrainingBook book) {
		this.book = book;
	}
	public TrainingBook getBook() {
		return book;
	}
}
