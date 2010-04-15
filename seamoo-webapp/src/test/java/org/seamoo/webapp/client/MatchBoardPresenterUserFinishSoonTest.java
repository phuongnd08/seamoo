package org.seamoo.webapp.client;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

@PrepareForTest(value={GWT.class, Window.Location.class, Timer.class})
public class MatchBoardPresenterUserFinishSoonTest extends PowerMockedGwtBddScenario {

	public MatchBoardPresenterUserFinishSoonTest() {
		super(new MatchBoardSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		// TODO Auto-generated method stub
		super.runScenario();
	}
	
	@BeforeMethod
	public void startStaticMock(){
		PowerMockito.mockStatic(GWT.class);
		PowerMockito.mockStatic(Window.Location.class);
		PowerMockito.mockStatic(Timer.class);
	}
}
