package org.seamoo.test;

import org.seamoo.utils.TimeProvider;

public class MockedTimeProvider extends TimeProvider {
	private int currentMonth;
	private int currentYear;
	private long currentTimeStamp = -1;// does not mock timeStamp by default

	public MockedTimeProvider() {
	}

	public MockedTimeProvider(int mockedYear, int mockedMonth) {
		this();
		this.setCurrentYear(mockedYear);
		this.setCurrentMonth(mockedMonth);
	}

	@Override
	public int getCurrentYear() {
		return currentYear;
	}

	@Override
	public int getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentTimeStamp(long currentTimeStamp) {
		this.currentTimeStamp = currentTimeStamp;
	}

	public long getCurrentTimeStamp() {
		if (currentTimeStamp == -1)
			return System.currentTimeMillis();
		return currentTimeStamp;
	}

	public void setCurrentYear(int currentYear) {
		this.currentYear = currentYear;
	}

	public void setCurrentMonth(int currentMonth) {
		this.currentMonth = currentMonth;
	}
}