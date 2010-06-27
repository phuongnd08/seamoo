package org.seamoo.webapp.integration.user;

import org.seamoo.webapp.integration.shared.SeleneseTestCfgAwareTestNgHelper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SampleLeaguePageIntegrationTest extends SeleneseTestCfgAwareTestNgHelper {

	@BeforeMethod
	public void openSampleLeaguePage() {
		selenium.open("/leagues/2/sample-league");
		selenium.waitForPageToLoad("30000");
	}

	@Test
	public void sampleSubjectPageShouldHaveTitleAndContainLinkToMatches() throws InterruptedException {		
		verifyEquals(selenium.getTitle(), "Sample League - SeaMoo");
		assertTrue(selenium.isElementPresent("link=Xem"));

	}

	@Test
	public void clickOnMatchLinkShouldRequireUserToLogin() {
		selenium.deleteAllVisibleCookies();
		selenium.click("link=Xem");
		selenium.waitForPageToLoad("30000");
		verifyEquals(selenium.getTitle(), "Đăng nhập với Open ID của bạn - SeaMoo");

	}
}
