package org.seamoo.webapp.integration.shared;

import static org.testng.Assert.*;

import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlUnitSteps {
	private WebClient client;

	public HtmlUnitSteps() {
		client = new WebClient();
		// client.setAjaxController(new NicelyResynchronizingAjaxController());
	}

	protected HtmlPage loadPage(String relativeUrl) {
		try {
			return (HtmlPage) client.getPage(TestConfig.getServerBase() + relativeUrl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	protected WebClient getWebClient() {
		return client;
	}

	protected void assertElementVisibility(HtmlElement element, boolean visible) {
		assertEquals(getElementVisibilityRecursive(element), visible);
	}

	protected boolean getElementVisibility(HtmlElement element) {
		return !element.getAttribute("style").contains("display: none");
	}

	protected boolean getElementVisibilityRecursive(HtmlElement element) {
		boolean visible = getElementVisibility(element);
		if (visible) {
			if (element.getParentNode() == element.getPage().getDocumentElement())
				return visible;
			else
				return getElementVisibilityRecursive((HtmlElement) element.getParentNode());
		} else
			return false;
	}

	protected final static int WAIT_PERIOD = 500;

	protected static void waitForPageLoad(HtmlPage page, HtmlUnitChecker checker, int maxMilliseconds)
			throws InterruptedException, TimeoutException {
		int waitTime = 0;
		while (waitTime < maxMilliseconds) {
			if (checker.isSatisfied()) {
				return;
			}
			synchronized (page) {
				Logger.getLogger(HtmlUnitSteps.class.getName()).info("Wait");
				page.wait(WAIT_PERIOD);
			}
			waitTime += WAIT_PERIOD;
		}
		throw new TimeoutException("Cannot wait for page load");
	}
}