package org.seamoo.webapp.client.user.ui;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.webapp.client.shared.ui.UiObjectFactory;
import org.seamoo.webapp.client.user.PowerMockedGwtBddScenario;
import org.testng.annotations.Test;

import com.google.gwt.core.client.GWT;

@PrepareForTest( { GWT.class, UiObjectFactory.class })
public class MultipleChoicesQuestionViewTest extends PowerMockedGwtBddScenario {
	public MultipleChoicesQuestionViewTest() {
		super(new MultipleChoicesQuestionViewSteps());
	}

	@Override
	@Test
	public void runScenario() throws Throwable {
		super.runScenario();
	}

}
