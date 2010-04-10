package org.seamoo.webapp.client.admin;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.webapp.client.PowerMockedGwtBddScenario;
import org.testng.annotations.Test;

import com.google.gwt.user.client.Window;

//mocking Window.confirm method
@PrepareForTest(Window.class)
public class LeagueListPresenterTest extends PowerMockedGwtBddScenario {

	public LeagueListPresenterTest() {
		super(new LeagueListPresenterSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}
}
