package org.seamoo.webapp.integration.admin;

import org.seamoo.webapp.integration.BddScenario;
import org.seamoo.webapp.integration.admin.steps.SiteSettingsSteps;
import org.testng.annotations.Test;

public class SiteSettingsUpdateSuccessfully extends BddScenario {

	@Test
	public void runScenario() throws Throwable {
		super.runScenario();
	}

	public SiteSettingsUpdateSuccessfully() {
		super(new SiteSettingsSteps());
	}
}
