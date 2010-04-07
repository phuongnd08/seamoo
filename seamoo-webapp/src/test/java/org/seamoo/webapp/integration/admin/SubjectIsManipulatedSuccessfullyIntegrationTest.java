package org.seamoo.webapp.integration.admin;

import org.seamoo.webapp.integration.BddScenario;
import org.testng.annotations.Test;

public class SubjectIsManipulatedSuccessfullyIntegrationTest extends BddScenario {

	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}

	public SubjectIsManipulatedSuccessfullyIntegrationTest() {
		super(new ManageSubjectSteps());
	}
}
