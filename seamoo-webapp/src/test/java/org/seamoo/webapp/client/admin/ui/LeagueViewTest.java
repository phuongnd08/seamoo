package org.seamoo.webapp.client.admin.ui;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.webapp.client.user.PowerMockedGwtBddScenario;
import org.testng.annotations.Test;

import com.google.gwt.core.client.GWT;

@PrepareForTest(GWT.class)
public class LeagueViewTest extends PowerMockedGwtBddScenario {
	public LeagueViewTest() {
		super(new LeagueViewSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}

}
