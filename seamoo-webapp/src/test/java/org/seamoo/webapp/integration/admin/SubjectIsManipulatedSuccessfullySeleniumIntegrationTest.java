package org.seamoo.webapp.integration.admin;

import org.seamoo.webapp.integration.shared.SeleneseTestCfgAwareTestNgHelper;
import org.testng.annotations.Test;

public class SubjectIsManipulatedSuccessfullySeleniumIntegrationTest extends SeleneseTestCfgAwareTestNgHelper {

	@Test
	public void buttonContainerShouldBeShownHideAccordingly() throws Exception {
		selenium.open("/admin/subjects");
		while (selenium.isElementPresent("//div[@id='loading-message']")) {
			selenium.wait(100);
		}
		// click edit button
		selenium.click("//div[@id='subject-list']/div/div[1]/form/table/tbody/tr[1]/td[2]/div[1]/div[1]/div/a[1]");
		// button container should be visible
		assertTrue(selenium.isVisible("//div[@id='subject-list']/div/div[1]/form/table/tbody/tr[2]/td/div"));
		// click cancel button
		selenium.click("//div[@id='subject-list']/div/div[1]/form/table/tbody/tr[2]/td/div/button[3]");
		// button container should be visible
		assertFalse(selenium.isVisible("//div[@id='subject-list']/div/div[1]/form/table/tbody/tr[2]/td/div"));
	}
}
