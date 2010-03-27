package org.seamoo.webapp.integration.admin.subjects;

import org.seamoo.webapp.integration.BDDScenario;
import org.seamoo.webapp.integration.steps.ManageSubjectSteps;
import org.testng.annotations.Test;

public class SubjectIsAvailable extends BDDScenario {

	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}

	public SubjectIsAvailable() {
		super(new ManageSubjectSteps());
	}

}
