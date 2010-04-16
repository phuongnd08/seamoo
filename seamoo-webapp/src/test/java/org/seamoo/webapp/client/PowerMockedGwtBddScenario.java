package org.seamoo.webapp.client;

import org.seamoo.test.PowerMockedBddScenario;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.google.gwt.junit.GWTMockUtilities;

public abstract class PowerMockedGwtBddScenario extends PowerMockedBddScenario {
	public PowerMockedGwtBddScenario(Object... stepsInstance) {
		super(stepsInstance);
	}

	@BeforeMethod
	public void gwtDisarm() {
		GWTMockUtilities.disarm();
	}

	@AfterMethod
	public void gwtRestore() {
		GWTMockUtilities.restore();
	}
}
