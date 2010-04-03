package org.seamoo.webapp.client.admin.ui;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.webapp.client.PowerMockedGwtBddScenario;
import org.testng.annotations.Test;

import com.google.gwt.core.client.GWT;

@PrepareForTest(GWT.class)
public class SubjectViewTest extends PowerMockedGwtBddScenario {
	public SubjectViewTest() {
		super(new SubjectViewSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}

}
