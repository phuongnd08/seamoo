package org.seamoo.test;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class PowerMockedGaeBddScenario extends PowerMockedBddScenario {

	LocalServiceTestHelper helper;

	public PowerMockedGaeBddScenario(Object... stepInstances) {
		super(stepInstances);
		helper = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig(), new LocalDatastoreServiceTestConfig());
	}

	@BeforeMethod
	public void setUpGaeEnv() {
		helper.setUp();
	}

	@AfterMethod
	public void testDownGaeEvn() {
		// helper.tearDown();
	}
}
