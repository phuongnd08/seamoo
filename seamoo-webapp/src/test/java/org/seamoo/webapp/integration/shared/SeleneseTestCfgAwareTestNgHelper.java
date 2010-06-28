package org.seamoo.webapp.integration.shared;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.thoughtworks.selenium.SeleneseTestNgHelper;

public class SeleneseTestCfgAwareTestNgHelper extends SeleneseTestNgHelper {
	@BeforeTest
	@Override
	@Parameters( { "selenium.url", "selenium.browser" })
	public void setUp(@Optional String url, @Optional String browserString) throws Exception {
		super.setUp(TestCfg.getServerBase(), "*firefox");
		installSamplesOnce();
	}

	static boolean sampleInstalled = false;

	@BeforeTest
	public void installSamplesOnce() throws InterruptedException {
		if (!sampleInstalled && selenium != null) {
			selenium.open("/demo/install-samples");
			selenium.waitForPageToLoad("30000");
			sampleInstalled = true;
		}
	}
	
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
