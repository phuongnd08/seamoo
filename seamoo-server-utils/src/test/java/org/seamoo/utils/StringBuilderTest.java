package org.seamoo.utils;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

;

public class StringBuilderTest {

	@Test
	public void testJoin() {
		assertEquals(StringBuilder.join(new String[] { "a", "b" }, ""), "ab");
		assertEquals(StringBuilder.join(new String[] { "a", "b" }, "x"), "axb");
	}

	@Test
	public void testTimes() {
		assertEqualsNoOrder(StringBuilder.times("x", 2), new Object[] { "x", "x" });
	}
}
