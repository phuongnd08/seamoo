package org.seamoo.webapp.client.user.ui;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.webapp.client.shared.ui.UiObjectFactory;
import org.seamoo.webapp.client.user.PowerMockedGwtBddScenario;
import org.testng.annotations.Test;

import com.google.gwt.core.client.GWT;

@PrepareForTest( { GWT.class, UiObjectFactory.class })
public class FollowPatternQuestionViewTest extends PowerMockedGwtBddScenario {
	public FollowPatternQuestionViewTest() {
		super(new FollowPatternQuestionViewSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		super.runScenario();
	}
}
