package org.seamoo.webapp.client.ui;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.webapp.client.PowerMockedGwtBddScenario;
import org.testng.annotations.Test;

import com.google.gwt.core.client.GWT;

@PrepareForTest( { GWT.class, UiObjectFactory.class })
public class MatchViewTest extends PowerMockedGwtBddScenario {
	public MatchViewTest() {
		super(new MatchViewSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}

}
