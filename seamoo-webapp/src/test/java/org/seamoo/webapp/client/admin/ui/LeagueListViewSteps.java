package org.seamoo.webapp.client.admin.ui;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.lang.reflect.Field;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueDisplay;
import org.seamoo.webapp.client.admin.ui.LeagueListView.LeagueListViewUiBinder;
import org.seamoo.webapp.client.admin.ui.LeagueView.LeagueViewUiBinder;
import org.seamoo.webapp.client.uimocker.GwtUiMocker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class LeagueListViewSteps {
	LeagueListView leagueListView;

	@Given("A LeagueListView")
	public void initLeagueListView() {
		PowerMockito.mockStatic(GWT.class);
		when(GWT.create(LeagueListViewUiBinder.class)).thenReturn(new LeagueListViewUiBinder() {

			@Override
			public FlowPanel createAndBindUi(LeagueListView leagueListView) {
				// TODO Auto-generated method stub
				return GwtUiMocker.mockUiField(leagueListView, FlowPanel.class);
			}
		});

		when(GWT.create(LeagueViewUiBinder.class)).thenReturn(new LeagueViewUiBinder() {

			@Override
			public Widget createAndBindUi(LeagueView leagueView) {
				// TODO Auto-generated method stub
				return GwtUiMocker.mockUiField(leagueView, Widget.class);
			}
		});

		leagueListView = new LeagueListView();
	}

	LeagueDisplay leagueDisplay;

	private Widget getWidget(String widgetName) {
		try {
			Field field = null;
			field = LeagueListView.class.getDeclaredField(widgetName);
			field.setAccessible(true);
			return (Widget) field.get(leagueListView);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@When("createLeagueDisplay is called")
	public void createLeagueDisplay() {
		leagueDisplay = leagueListView.createLeagueDisplay();
	}

	@When("addLeagueDisplay is called")
	public void addLeagueDisplay() {
		leagueListView.addLeagueDisplay(leagueDisplay);
	}

	@When("removeLeagueDisplay is called")
	public void removeLeagueDisplay() {
		leagueListView.removeLeagueDisplay(leagueDisplay);
	}

	@Then("A new LeagueView is instantiated")
	public void assertLeagueDisplayIsLeagueView() {
		assertEquals(leagueDisplay.getClass(), LeagueView.class);
	}

	@Then("mainPanel.$method is called")
	public void assertMainPanelMethodCalled(String method) {
		FlowPanel panel = (FlowPanel) getWidget("mainPanel");
		if (method.equals("add"))
			verify(panel).add((Widget) leagueDisplay);
		else if (method.equals("remove"))
			verify(panel).remove((Widget) leagueDisplay);
	}
}
