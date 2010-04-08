package org.seamoo.webapp.integration.shared;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.thoughtworks.selenium.SeleneseTestNgHelper;

public class SeleneseTestCfgAwareTestNgHelper extends SeleneseTestNgHelper {
	@BeforeTest
	@Override
	@Parameters( { "selenium.url", "selenium.browser" })
	// trick: To have setUp method override properly
	public void setUp(@Optional String url, @Optional String browserString) throws Exception {
		super.setUp(TestConfig.getServerBase(), "*firefox");
	}

}
