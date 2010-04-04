package org.seamoo.webapp.client.admin.ui;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.lang.reflect.Field;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.webapp.client.admin.SubjectListPresenter.SubjectDisplay;
import org.seamoo.webapp.client.admin.ui.SubjectListView.SubjectListViewUiBinder;
import org.seamoo.webapp.client.admin.ui.SubjectView.SubjectViewUiBinder;
import org.seamoo.webapp.client.uimocker.GwtUiMocker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class SubjectListViewSteps {
	SubjectListView subjectListView;

	@Given("A SubjectListView")
	public void initSubjectListView() {
		PowerMockito.mockStatic(GWT.class);
		when(GWT.create(SubjectListViewUiBinder.class)).thenReturn(new SubjectListViewUiBinder() {

			@Override
			public FlowPanel createAndBindUi(SubjectListView subjectListView) {
				// TODO Auto-generated method stub
				return GwtUiMocker.mockUiField(subjectListView, FlowPanel.class);
			}
		});

		when(GWT.create(SubjectViewUiBinder.class)).thenReturn(new SubjectViewUiBinder() {

			@Override
			public Widget createAndBindUi(SubjectView subjectView) {
				// TODO Auto-generated method stub
				return GwtUiMocker.mockUiField(subjectView, Widget.class);
			}
		});

		subjectListView = new SubjectListView();
	}

	SubjectDisplay subjectDisplay;

	private Widget getWidget(String widgetName) {
		try {
			Field field = null;
			field = SubjectListView.class.getDeclaredField(widgetName);
			field.setAccessible(true);
			return (Widget) field.get(subjectListView);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@When("createSubjectDisplay is called")
	public void createSubjectDisplay() {
		subjectDisplay = subjectListView.createSubjectDisplay();
	}

	@When("addSubjectDisplay is called")
	public void addSubjectDisplay() {
		subjectListView.addSubjectDisplay(subjectDisplay);
	}

	@When("removeSubjectDisplay is called")
	public void removeSubjectDisplay() {
		subjectListView.removeSubjectDisplay(subjectDisplay);
	}

	@Then("A new SubjectView is instantiated")
	public void assertSubjectDisplayIsSubjectView() {
		assertEquals(subjectDisplay.getClass(), SubjectView.class);
	}

	@Then("mainPanel.$method is called")
	public void assertMainPanelMethodCalled(String method) {
		FlowPanel panel = (FlowPanel) getWidget("mainPanel");
		if (method.equals("add"))
			verify(panel).add((Widget) subjectDisplay);
		else if (method.equals("remove"))
			verify(panel).remove((Widget) subjectDisplay);
	}
}
