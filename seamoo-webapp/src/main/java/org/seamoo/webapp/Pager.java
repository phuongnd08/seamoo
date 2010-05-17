package org.seamoo.webapp;

import java.util.ArrayList;
import java.util.List;

public class Pager {
	public static final long MAX_DISPLAYED_PAGE = 10;
	public static final long DISPLAYED_LEFT_SIDE = MAX_DISPLAYED_PAGE / 2 - 1;
	public static final long DISPLAYED_RIGHT_SIDE = MAX_DISPLAYED_PAGE - 1 - DISPLAYED_LEFT_SIDE;

	public static List<Long> getPages(long min, long max, long current) {
		List<Long> pages = new ArrayList<Long>();
		long from = current - DISPLAYED_LEFT_SIDE;
		long to = current + DISPLAYED_RIGHT_SIDE;
		if (from < min) {
			from = min;
			to = Math.min(max, from + MAX_DISPLAYED_PAGE - 1);
		} else if (to > max) {
			to = max;
			from = Math.max(min, to - MAX_DISPLAYED_PAGE + 1);
		}
		for (long i = from; i <= to; i++)
			pages.add(i);
		return pages;
	}

	public static long getPageCount(long itemCount, int itemPerPage) {
		if (itemCount==0)return 1L;//always have atleast 1 page
		return (long) (Math.ceil(((double) itemCount) / itemPerPage));
	}

	public static long getFromForPage(long page, int itemPerPage) {
		return (page - 1) * itemPerPage;
	}
}
