package org.seamoo.webapp.integration.admin;

import org.seamoo.webapp.integration.BddScenario;
import org.testng.annotations.Test;

public class SiteSettingsUpdateSuccessfullyIntegrationTest extends BddScenario {

	@Test
	public void runScenario() throws Throwable {
		super.runScenario();
	}

	public SiteSettingsUpdateSuccessfullyIntegrationTest() {
		super(new SiteSettingsSteps());
	}
}
