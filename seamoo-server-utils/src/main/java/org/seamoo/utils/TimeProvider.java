package org.seamoo.utils;

import java.util.Date;

public class TimeProvider {
	public static final TimeProvider DEFAULT = new TimeProvider();
	public static final long MILLISECONDS_PER_DAY = 24 * 60 * 60 * 1000;

	public long getCurrentTimeStamp() {
		return System.currentTimeMillis();
	}

	/**
	 * Return current year (YYYY)
	 * 
	 * @return
	 */
	public int getCurrentYear() {
		return (new Date()).getYear() + 1900;
	}

	/**
	 * Return current month (1-12)
	 * 
	 * @return
	 */
	public int getCurrentMonth() {
		return (new Date()).getMonth() + 1;
	}
	
	public Date getCurrentDate(){
		return new Date();
	}

	public int getDaysTillEndOfMonth() {
		Date now = getCurrentDate();
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		int year = now.getYear();
		int month = now.getMonth() + 1;
		if (month == 12)
			month = 0;
		Date startOfNextMonth = new Date(year, month, 1);
		return (int) ((startOfNextMonth.getTime() - today.getTime()) / MILLISECONDS_PER_DAY);
	}
}
