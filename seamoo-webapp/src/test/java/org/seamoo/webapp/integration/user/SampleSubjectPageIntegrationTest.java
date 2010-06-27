package org.seamoo.webapp.integration.user;

import org.seamoo.webapp.integration.shared.SeleneseTestCfgAwareTestNgHelper;
import org.testng.annotations.Test;

public class SampleSubjectPageIntegrationTest extends SeleneseTestCfgAwareTestNgHelper {

	@Test
	public void sampleSubjectPageShouldHaveTitleAndContainLinkToLeagues() {
		selenium.open("/subjects/1/sample-subject");
		verifyEquals(selenium.getTitle(), "Sample Subject - SeaMoo");
		assertEquals(selenium.getText("link=Sample League"), "Sample League");

	}

	@Test
	public void clickOnLeagueShouldOpenLeaguePage() {
		selenium.open("/subjects/1/sample-subject");
		selenium.click("link=Sample League");
		selenium.waitForPageToLoad("30000");
		verifyEquals(selenium.getTitle(), "Sample League - SeaMoo");
	}
}
