package org.seamoo.webapp.integration;

import java.io.IOException;
import java.net.MalformedURLException;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HomePageIT extends ITBase {
	@Test
	public void homePageShouldHaveTitleSetToWelcome()
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		HtmlPage page = loadGETWebResponse("/");
		assertEquals(page.getTitleText(), "Welcome - SeaMoo");
	}
}
