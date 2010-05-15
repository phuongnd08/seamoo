package org.seamoo.competition;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.test.PowerMockedGaeBddScenario;
import org.testng.annotations.Test;

@PrepareForTest(value = { EntityFactory.class })
public class MatchOrganizerFinishedMomentAssignedWhenMatchFinishedTest extends PowerMockedGaeBddScenario {
	public MatchOrganizerFinishedMomentAssignedWhenMatchFinishedTest() {
		super(new MatchOrganizerSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}
}
