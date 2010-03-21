package org.seamoo.webapp.integration;

import java.io.IOException;
import java.util.Properties;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ITBase {
	protected static Properties testProperties;

	static {
		testProperties = new Properties();
		try {
			testProperties.load(ITBase.class
					.getResourceAsStream("it-test.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected HtmlPage loadGETWebResponse(String relativeUrl) {
		WebClient client = new WebClient();
		try {
			return (HtmlPage) client.getPage(getServerBase() + relativeUrl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	protected String getServerBase() {
		return testProperties.getProperty("serverBase");
	}
}
