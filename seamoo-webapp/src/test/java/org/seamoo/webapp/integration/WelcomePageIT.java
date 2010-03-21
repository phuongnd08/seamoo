package org.seamoo.webapp.integration;

import java.io.IOException;
import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlLink;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WelcomePageIT extends ITBase {
	@Test
	public void welcomePageShouldContainsLinkToSubjectPages()
			throws SAXException, FailingHttpStatusCodeException,
			MalformedURLException, IOException {
		HtmlPage page = loadGETWebResponse("/welcome.html");
		HtmlAnchor listOfSubjectLink = (HtmlAnchor) page.getByXPath(
				"//a[contains(text(), 'List of subjects')]").get(0);
		Assert.assertNotNull(listOfSubjectLink);
	}
}
