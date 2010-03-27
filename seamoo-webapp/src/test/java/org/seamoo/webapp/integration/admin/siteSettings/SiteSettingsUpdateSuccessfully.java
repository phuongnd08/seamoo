package org.seamoo.webapp.integration.admin.siteSettings;

import org.seamoo.webapp.integration.BDDScenario;
import org.seamoo.webapp.integration.steps.SiteSettingsSteps;
import org.testng.annotations.Test;

public class SiteSettingsUpdateSuccessfully extends BDDScenario {

	@Test
	public void runScenario() throws Throwable {
		super.runScenario();
	}

	public SiteSettingsUpdateSuccessfully() {
		super(new SiteSettingsSteps());
	}
}
