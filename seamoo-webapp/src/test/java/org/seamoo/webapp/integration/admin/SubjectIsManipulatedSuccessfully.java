package org.seamoo.webapp.integration.admin;

import org.seamoo.webapp.integration.BddScenario;
import org.seamoo.webapp.integration.admin.steps.ManageSubjectSteps;
import org.testng.annotations.Test;

public class SubjectIsManipulatedSuccessfully extends BddScenario {

	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}

	public SubjectIsManipulatedSuccessfully() {
		super(new ManageSubjectSteps());
	}
}
