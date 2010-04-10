package org.seamoo.webapp.client.admin.ui;

import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueDisplay;
import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueListDisplay;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class LeagueListView extends Composite implements LeagueListDisplay {
	interface LeagueListViewUiBinder extends UiBinder<FlowPanel, LeagueListView> {
	}

	private static final LeagueListViewUiBinder binder = GWT.create(LeagueListViewUiBinder.class);

	@UiField
	FlowPanel mainPanel;

	public LeagueListView() {
		initWidget(binder.createAndBindUi(this));
	}

	@Override
	public void addLeagueDisplay(LeagueDisplay leagueDisplay) {
		// TODO Auto-generated method stub
		mainPanel.add((Widget) leagueDisplay);
	}

	@Override
	public void removeLeagueDisplay(LeagueDisplay leagueDisplay) {
		// TODO Auto-generated method stub
		mainPanel.remove((Widget) leagueDisplay);
	}

	@Override
	public LeagueDisplay createLeagueDisplay() {
		// TODO Auto-generated method stub
		return new LeagueView();
	}

}
