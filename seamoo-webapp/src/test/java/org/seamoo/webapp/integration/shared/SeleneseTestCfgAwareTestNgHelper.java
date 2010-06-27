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

}
