package org.seamoo.webapp.client.admin.ui;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.webapp.client.user.PowerMockedGwtBddScenario;
import org.testng.annotations.Test;

import com.google.gwt.core.client.GWT;

@PrepareForTest(GWT.class)
public class SubjectListViewTest extends PowerMockedGwtBddScenario {
	public SubjectListViewTest() {
		super(new SubjectListViewSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}

}