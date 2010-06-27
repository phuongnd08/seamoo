package org.seamoo.webapp.controllers;

import org.seamoo.webapp.client.user.PowerMockedGwtBddScenario;
import org.testng.annotations.Test;

public class UserControllerTest extends PowerMockedGwtBddScenario {
	public UserControllerTest() {
		super(new UserControllerSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}
}
