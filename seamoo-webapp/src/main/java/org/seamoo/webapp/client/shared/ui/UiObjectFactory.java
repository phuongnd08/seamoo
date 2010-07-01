package org.seamoo.webapp.client.shared.ui;

import org.seamoo.webapp.client.user.ui.CompetitorView;

import com.google.gwt.user.client.ui.Button;

public class UiObjectFactory {
	public static Button newButton() {
		return new Button();
	}

	public static CompetitorView newCompetitorView() {
		return new CompetitorView();
	}
}
