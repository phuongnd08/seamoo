package org.seamoo.webapp.integration.user;

import org.seamoo.webapp.integration.shared.SeleneseTestCfgAwareTestNgHelper;
import org.testng.annotations.Test;

public class HomePageIntegrationTest extends SeleneseTestCfgAwareTestNgHelper {

	@Test
	public void homePageShouldHaveTitleAndNeccessaryLinks(){
		selenium.open("/");
		assertTrue(selenium.isElementPresent("link=Sàn đấu trí"));
		verifyEquals(selenium.getTitle(), "Sàn đấu trí - SeaMoo");
		assertTrue(selenium.isElementPresent("link=login"));
		assertTrue(selenium.isElementPresent("link=about"));
		assertTrue(selenium.isElementPresent("link=faq"));
		assertEquals(selenium.getText("link=Sample Subject"), "Sample Subject");
	}
	
	@Test
	public void fromHomePageCanGoToSubjectPage(){
		selenium.open("/");
		selenium.click("link=Sample Subject");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Sample Subject"));
	}
}
