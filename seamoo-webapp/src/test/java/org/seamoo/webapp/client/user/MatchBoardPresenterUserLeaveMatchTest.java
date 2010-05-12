package org.seamoo.webapp.client.user;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.webapp.client.shared.ui.MessageBox;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

@PrepareForTest(value = { GWT.class, Window.Location.class, Timer.class, MessageBox.class })
public class MatchBoardPresenterUserLeaveMatchTest extends PowerMockedGwtBddScenario {

	public MatchBoardPresenterUserLeaveMatchTest() {
		super(new MatchBoardPresenterSteps());
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
		PowerMockito.mockStatic(Timer.class);
	}
}
