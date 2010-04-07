package org.seamoo.webapp.client.admin;

import org.seamoo.webapp.integration.BddScenario;
import org.testng.annotations.Test;

public class SubjectServiceAsyncIntegrationTest extends BddScenario {

	public SubjectServiceAsyncIntegrationTest() {
		super(new SubjectServiceAsyncSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}
}
