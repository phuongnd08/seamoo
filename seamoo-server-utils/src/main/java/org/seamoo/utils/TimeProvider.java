package org.seamoo.utils;

import java.util.Date;

public class TimeProvider {
	public static final TimeProvider DEFAULT = new TimeProvider();

	public static long getCurrentTimeMilliseconds() {
		return System.currentTimeMillis();
	}

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
}
