package org.seamoo.webapp.integration.shared;

import org.testng.annotations.BeforeMethod;

public class AutoLogOutSeleneseTest extends SeleneseTestCfgAwareTestNgHelper {
	@BeforeMethod
	public void logOut() {
		// clear cookie on the open id provider page
		selenium.open("http://localhost:3000/");
		selenium.waitForPageToLoad("30000");
		selenium.deleteAllVisibleCookies();
		// clear cookie on the current site
		selenium.open("/");
		selenium.waitForPageToLoad("30000");
		selenium.deleteAllVisibleCookies();
	}
	
	public void loginAs(String user){
		selenium.type("openid_identifier", "http://localhost:3000");
		selenium.click("openid_submit");
		selenium.waitForPageToLoad("30000");
		selenium.type("id_to_send", user);
		selenium.click("yes");
		selenium.waitForPageToLoad("30000");
	}
}
