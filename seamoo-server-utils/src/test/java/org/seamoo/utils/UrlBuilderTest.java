package org.seamoo.utils;

import static org.testng.Assert.*;

import java.util.Arrays;

import org.testng.annotations.Test;

public class UrlBuilderTest {

	@Test
	public void getQueryStringShouldReturnCorrectly() {
		assertEquals("a=b", UrlBuilder.getQueryString(Arrays.asList(new UrlParameter("a", "b"))));
		assertEquals("a=b&c=d", UrlBuilder.getQueryString(Arrays.asList(new UrlParameter("a", "b"), new UrlParameter("c", "d"))));
	}
}
