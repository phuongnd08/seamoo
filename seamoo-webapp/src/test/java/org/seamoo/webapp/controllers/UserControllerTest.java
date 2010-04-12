package org.seamoo.webapp.controllers;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.webapp.client.PowerMockedGwtBddScenario;
import org.testng.annotations.Test;

import com.dyuproject.openid.OpenIdUser;
import com.dyuproject.openid.RelyingParty;

@PrepareForTest(value = { RelyingParty.class, OpenIdUser.class })
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
