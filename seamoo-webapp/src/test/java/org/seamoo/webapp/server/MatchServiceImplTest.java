package org.seamoo.webapp.server;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.test.PowerMockedBddScenario;
import org.seamoo.utils.TimeProvider;
import org.testng.annotations.Test;
import org.workingonit.gwtbridge.ServletUtils;

@PrepareForTest({ServletUtils.class, TimeProvider.class})
public class MatchServiceImplTest extends PowerMockedBddScenario{

	public MatchServiceImplTest() {
		super(new MatchServiceImplSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}
}
