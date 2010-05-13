package org.seamoo.competition;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.test.PowerMockedGaeBddScenario;
import org.seamoo.utils.TimeProvider;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@PrepareForTest(value = { EntityFactory.class, TimeProvider.class })
public class MatchOrganizerDisconnectedFinishedUserIsNotExcludedFromMatchTest extends PowerMockedGaeBddScenario {
	public MatchOrganizerDisconnectedFinishedUserIsNotExcludedFromMatchTest() {
		super(new MatchOrganizerSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}

	@BeforeMethod
	public void startStaticMock() {
		PowerMockito.mockStatic(TimeProvider.class);
	}

	@AfterMethod
	public void endStaticMock() {
	}

}
