package org.seamoo.webapp.integration;

import java.io.IOException;
import java.net.MalformedURLException;

import org.seamoo.webapp.integration.shared.HtmlUnitSteps;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HomePageIntegrationTest extends HtmlUnitSteps {
	@Test
	public void homePageShouldHaveTitleSetToWelcome()
			throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		HtmlPage page = loadPage("/");
		assertEquals(page.getTitleText(), "Welcome - SeaMoo");
	}
}
