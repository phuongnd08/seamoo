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
	
	@Test
	public void appendQueryParameterShouldReturnQueryCorrectly(){
		assertEquals("http://localhost:3000/login?a=b&c=d", UrlBuilder.appendQueryParameter("http://localhost:3000/login", "a=b", new UrlParameter("c", "d")));
		assertEquals("http://localhost:3000/login?a=b&c=d", UrlBuilder.appendQueryParameter("http://localhost:3000/login", "a=b", "c=d"));
		assertEquals("http://localhost:3000/login?c=d", UrlBuilder.appendQueryParameter("http://localhost:3000/login", "", "c=d"));
		assertEquals("http://localhost:3000/login?c=d", UrlBuilder.appendQueryParameter("http://localhost:3000/login", null, "c=d"));
	}
}
