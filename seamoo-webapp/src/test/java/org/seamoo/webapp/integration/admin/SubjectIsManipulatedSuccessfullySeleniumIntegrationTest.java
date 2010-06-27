package org.seamoo.webapp.integration.admin;

import org.seamoo.webapp.integration.shared.AutoLogOutSeleneseTest;
import org.seamoo.webapp.integration.shared.SeleneseTestCfgAwareTestNgHelper;
import org.testng.annotations.Test;

public class SubjectIsManipulatedSuccessfullySeleniumIntegrationTest extends AutoLogOutSeleneseTest {

	@Test 
	public void loggingInShouldBeRequired(){
		selenium.open("/admin/site-settings");
		verifyEquals(selenium.getTitle(), "Đăng nhập với Open ID của bạn - SeaMoo");
	}
	@Test
	public void siteSettingsCanBeSwitchedCorrectlyOnceUserLoggedInAsMrCold() throws Exception {
		selenium.open("/admin/site-settings");
		loginAs("mrcold");
		verifyTrue(selenium.isTextPresent("Site đang ở chế độ Khai trương"));
		selenium.click("//button[@type='submit']");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Site đang ở chế độ Bình thường"));
		selenium.click("//button[@type='submit']");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Site đang ở chế độ Khai trương"));
	}
}
