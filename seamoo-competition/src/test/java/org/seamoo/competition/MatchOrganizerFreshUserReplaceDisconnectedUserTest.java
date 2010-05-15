package org.seamoo.competition;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.test.PowerMockedGaeBddScenario;
import org.testng.annotations.Test;

@PrepareForTest(value = { EntityFactory.class})
public class MatchOrganizerFreshUserReplaceDisconnectedUserTest extends PowerMockedGaeBddScenario {
	public MatchOrganizerFreshUserReplaceDisconnectedUserTest() {
		super(new MatchOrganizerSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}

}
