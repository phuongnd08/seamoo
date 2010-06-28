package org.seamoo.webapp.integration.user;

import org.seamoo.webapp.integration.shared.AutoLogOutSeleneseTest;
import org.seamoo.webapp.integration.shared.SeleneseTestCfgAwareTestNgHelper;
import org.testng.annotations.Test;

public class HomePageIntegrationTest extends AutoLogOutSeleneseTest {

	@Test
	public void homePageShouldHaveTitleAndNeccessaryLinksWhenUSerIsNotLoggedIn() {
		selenium.open("/");
		verifyEquals(selenium.getTitle(), "Sàn đấu trí - SeaMoo");
		assertTrue(selenium.isElementPresent("link=Sàn đấu trí"));
		assertTrue(selenium.isElementPresent("link=login"));
		assertFalse(selenium.isElementPresent("link=logout"));
		assertTrue(selenium.isElementPresent("link=about"));
		assertTrue(selenium.isElementPresent("link=faq"));
		assertEquals(selenium.getText("link=Sample Subject"), "Sample Subject");
	}

	@Test
	public void homePageShouldShowIndicatorWhenUserIsLoggedIn() {
		selenium.open("/users/login");
		loginAs("mrcold");
		selenium.open("/");
		verifyEquals(selenium.getTitle(), "Sàn đấu trí - SeaMoo");
		assertTrue(selenium.isElementPresent("link=Sàn đấu trí"));
		assertTrue(selenium.isElementPresent("link=Mr Cold"));
		assertFalse(selenium.isElementPresent("link=login"));
		assertTrue(selenium.isElementPresent("link=logout"));
		assertTrue(selenium.isElementPresent("link=about"));
		assertTrue(selenium.isElementPresent("link=faq"));
		assertEquals(selenium.getText("link=Sample Subject"), "Sample Subject");
	}

	@Test
	public void fromHomePageCanGoToSubjectPage() {
		selenium.open("/");
		selenium.click("link=Sample Subject");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Sample Subject"));
	}
}
