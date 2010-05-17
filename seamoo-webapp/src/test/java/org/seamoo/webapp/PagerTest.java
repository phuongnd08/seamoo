package org.seamoo.webapp;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class PagerTest {
	@Test
	public void getPagesReturnedGoolgeStylePages() {
		assertEquals(Pager.getPages(1, 10, 3).toArray(), new Object[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L });
		assertEquals(Pager.getPages(1, 10, 9).toArray(), new Object[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L });
		assertEquals(Pager.getPages(1, 20, 9).toArray(), new Object[] { 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L });
	}

	@Test
	public void getPageCountTest() {
		assertEquals(Pager.getPageCount(20, 10), 2);
		assertEquals(Pager.getPageCount(21, 10), 3);
		assertEquals(Pager.getPageCount(29, 10), 3);
		assertEquals(Pager.getPageCount(0, 10), 1);
	}

	@Test
	public void getFromForPageTest() {
		assertEquals(Pager.getFromForPage(3, 10), 20);
	}
}
