package org.seamoo.webapp.client.admin;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.webapp.client.user.PowerMockedGwtBddScenario;
import org.testng.annotations.Test;

import com.google.gwt.user.client.Window;

//mocking Window.confirm method
@PrepareForTest(Window.class)
public class SubjectListPresenterTest extends PowerMockedGwtBddScenario {

	public SubjectListPresenterTest() {
		super(new SubjectListPresenterSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}
}
