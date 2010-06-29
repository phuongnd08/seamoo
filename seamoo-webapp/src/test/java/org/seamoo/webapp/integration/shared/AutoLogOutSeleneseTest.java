package org.seamoo.webapp.integration.shared;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class AutoLogOutSeleneseTest extends SeleneseTestCfgAwareTestNgHelper {
	@BeforeMethod
	public void setUpForAutoLogOutTest() {
		logOut();
		System.out.println("Logged Out");
	}

	@AfterMethod
	public void tearDownForAutoLogOutTest() {
		logOut();
		System.out.println("Logged Out");
	}
}
