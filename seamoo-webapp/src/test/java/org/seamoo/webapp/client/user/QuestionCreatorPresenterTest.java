package org.seamoo.webapp.client.user;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

@PrepareForTest(value = { GWT.class, Window.Location.class })
public class QuestionCreatorPresenterTest extends PowerMockedGwtBddScenario {

	public QuestionCreatorPresenterTest() {
		super(new QuestionCreatorPresenterSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}

	@BeforeMethod
	public void startStaticMock() {
		PowerMockito.mockStatic(GWT.class);
		PowerMockito.mockStatic(Window.Location.class);
	}

	@AfterMethod
	public void endStaticMock() {
	}
}
