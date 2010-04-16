package org.seamoo.competition;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.test.PowerMockedBddScenario;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@PrepareForTest(value = { MatchOrganizer.class, TimeStampProvider.class })
public class MatchOrganizerUserCannotJoinAfterMatchIsFullTest extends PowerMockedBddScenario {
	public MatchOrganizerUserCannotJoinAfterMatchIsFullTest() {
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
		PowerMockito.mockStatic(TimeStampProvider.class);
	}

	@AfterMethod
	public void endStaticMock() {
	}

}
