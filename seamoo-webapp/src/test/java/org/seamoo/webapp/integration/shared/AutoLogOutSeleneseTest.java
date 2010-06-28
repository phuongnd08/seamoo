package org.seamoo.webapp.integration.shared;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class AutoLogOutSeleneseTest extends SeleneseTestCfgAwareTestNgHelper {
	@BeforeMethod
	public void setUpForAutoLogOutTest() {
		logOut();
	}

	@AfterMethod
	public void tearDownForAutoLogOutTest() {
		logOut();
	}
}
