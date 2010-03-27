package org.seamoo.webapp.integration.admin.subjects;

import org.seamoo.webapp.integration.BDDScenario;
import org.seamoo.webapp.integration.steps.ManageSubjectSteps;
import org.testng.annotations.Test;

public class SubjectIsManipulatedSuccessfully extends BDDScenario {

	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}

	public SubjectIsManipulatedSuccessfully() {
		super(new ManageSubjectSteps());
	}
}
