package org.seamoo.utils;

import static org.testng.Assert.*;

import java.util.Date;

import org.testng.annotations.Test;

public class TimeProviderTest {
	private static class HardCodedTimeProvide extends TimeProvider {
		private Date now;

		public HardCodedTimeProvide(int year, int month, int dayOfMonth) {
			now = new Date(year - 1900, month - 1, dayOfMonth);
		}

		@Override
		public Date getCurrentDate() {
			return now;
		}
	}

	@Test
	public void getDaysTillEndOfMonthReturnCorrectNumber() {
		TimeProvider t1 = new HardCodedTimeProvide(2010, 5, 31);
		assertEquals(t1.getDaysTillEndOfMonth(), 1);
		TimeProvider t2 = new HardCodedTimeProvide(2010, 5, 30);
		assertEquals(t2.getDaysTillEndOfMonth(), 2);
		TimeProvider t3 = new HardCodedTimeProvide(2010, 2, 27);
		assertEquals(t3.getDaysTillEndOfMonth(), 2);
		TimeProvider t4 = new HardCodedTimeProvide(2008, 2, 27);
		assertEquals(t4.getDaysTillEndOfMonth(), 3);
	}
}
