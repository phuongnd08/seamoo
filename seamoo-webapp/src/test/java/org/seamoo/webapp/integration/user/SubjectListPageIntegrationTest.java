package org.seamoo.webapp.integration.user;

import org.seamoo.webapp.integration.shared.SeleneseTestCfgAwareTestNgHelper;
import org.testng.annotations.Test;

public class SubjectListPageIntegrationTest extends SeleneseTestCfgAwareTestNgHelper {

	@Test
	public void subjectListPageShouldContainSampleSubject() {
		selenium.open("/subjects");
		assertEquals(selenium.getText("link=Sample Subject"), "Sample Subject");
	}

	@Test
	public void clickOnSampleSubjectLinkShouldShowSampleSubjectPage() {
		selenium.open("/subjects");
		selenium.click("link=Sample Subject");
		selenium.waitForPageToLoad("30000");
		verifyTrue(selenium.isTextPresent("Sample Subject"));

	}
}
