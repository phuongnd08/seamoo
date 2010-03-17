package org.seamoo.entities.training;

import java.util.Date;

public class TrainingEntry {
	private Date addedTime;
	private Date lastRepetition;
	private Date nextRepetition;
	private long interval;
	private double easiness;
	private TrainingBook book;
	public void setAddedTime(Date addedTime) {
		this.addedTime = addedTime;
	}
	public Date getAddedTime() {
		return addedTime;
	}
	public void setLastRepetition(Date lastRepetition) {
		this.lastRepetition = lastRepetition;
	}
	public Date getLastRepetition() {
		return lastRepetition;
	}
	public void setNextRepetition(Date nextRepetition) {
		this.nextRepetition = nextRepetition;
	}
	public Date getNextRepetition() {
		return nextRepetition;
	}
	public void setInterval(long interval) {
		this.interval = interval;
	}
	public long getInterval() {
		return interval;
	}
	public void setEasiness(double easiness) {
		this.easiness = easiness;
	}
	public double getEasiness() {
		return easiness;
	}
	public void setBook(TrainingBook book) {
		this.book = book;
	}
	public TrainingBook getBook() {
		return book;
	}
}
