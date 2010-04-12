package org.seamoo.webapp.integration;

import java.io.IOException;
import java.net.MalformedURLException;

import org.seamoo.webapp.integration.shared.HtmlUnitSteps;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class UserPageIntegrationTest extends HtmlUnitSteps {
	@Test
	public void loginPageShouldRunFineWithoutReturnUrlSet() throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		HtmlPage page = loadPage("/users/login");
		assertEquals(page.getTitleText(), "Đăng nhập với Open ID của bạn - SeaMoo");
		assertFalse(page.asText().contains("FreeMarker template error"));
	}
}
