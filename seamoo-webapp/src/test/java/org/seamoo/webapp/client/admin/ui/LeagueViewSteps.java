package org.seamoo.webapp.client.admin.ui;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.jbehave.scenario.annotations.Alias;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.entities.League;
import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueDisplay.LeagueDisplayEventListener;
import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueDisplay.LeagueDisplayMode;
import org.seamoo.webapp.client.admin.ui.LeagueView.LeagueViewUiBinder;
import org.seamoo.webapp.client.uimocker.GwtUiMocker;
import org.seamoo.webapp.client.uimocker.MockedClickable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

public class LeagueViewSteps {
	LeagueView LeagueView;
	LeagueDisplayEventListener listener;
	League League;

	@Given("A LeagueView")
	public void initLeagueDisplay() {
		PowerMockito.mockStatic(GWT.class);
		when(GWT.create(LeagueViewUiBinder.class)).thenReturn(new LeagueViewUiBinder() {

			@Override
			public Widget createAndBindUi(LeagueView LeagueView) {
				// TODO Auto-generated method stub
				return GwtUiMocker.mockUiField(LeagueView, Widget.class);
			}
		});
		listener = mock(LeagueDisplayEventListener.class);
		LeagueView = new LeagueView();
		LeagueView.addEventListener(listener);
	}

	@Given("An $enabled League")
	@Alias("A $enabled League")
	public void initLeague(String enabled) {
		League = new League(1L, "English Amateur", "images/leagues/eng-amateur.png", "English for everyone", 1,
				enabled.equals("enabled"));
		LeagueView.setLeague(League);
	}

	@When("Mode is switched to $mode")
	public void switchMode(String mode) {
		LeagueView.setMode(LeagueDisplayMode.valueOf(mode));
	}

	private Widget getWidget(String widgetName) {
		try {
			Field field = null;
			field = LeagueView.class.getDeclaredField(widgetName);
			field.setAccessible(true);
			return (Widget) field.get(LeagueView);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Then("$widgets are visible")
	@Alias("$widgets is visible")
	public void assertFieldsShown(List<String> widgets) {
		assertFieldsVisibility(widgets, true);

	}

	@Then("$widgets are invisible")
	@Alias("$widgets is invisible")
	public void assertFieldsHidden(List<String> widgets) {
		assertFieldsVisibility(widgets, false);
	}

	public void assertFieldsVisibility(List<String> fields, boolean visible) {
		for (int i = 0; i < fields.size(); i++) {
			String field = fields.get(i);
			assertEquals(getWidget(field).isVisible(), visible, String.format("%s.visble <> %b", field, visible));
		}
	}

	@When("$widgetName is clicked")
	public void clickWidget(String widgetName) {
		Widget control = getWidget(widgetName);
		((MockedClickable) control).click();
	}

	@Then("$event event is triggered")
	public void assertEventTriggered(String event) {
		if (event.equals("edit"))
			verify(listener).edit(LeagueView);
		else if (event.equals("edit-cancel"))
			verify(listener).cancelEdit(LeagueView);
		else if (event.equals("delete"))
			verify(listener).delete(LeagueView, League);
		else if (event.equals("create"))
			verify(listener).create(eq(LeagueView), (League) any());
		else if (event.equals("update"))
			verify(listener).update(eq(LeagueView), (League) any());
		else if (event.equals("select-logo"))
			verify(listener).selectLogo(eq(LeagueView), (String) any());
		else
			throw new RuntimeException("Event " + event + " is not supported");
	}

	public static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	@Then("$property of $widget is \"$value\"")
	public void assertWidgetProperty(String property, String widget, String value) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Widget control = getWidget(widget);
		Method method = control.getClass().getMethod("get" + capitalize(property));
		Object actualValue = method.invoke(control);
		assertEquals(actualValue.toString(), value);
	}

	@When("$property of $widget is changed to \"$value\"")
	public void changeWidgetProperty(String property, String widget, String value) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object v = value.equals("true") ? true : value.equals("false") ? false : value;
		Widget control = getWidget(widget);
		Class<?> paramClass = property.equals("value") ? Object.class : String.class;
		Method method = control.getClass().getMethod("set" + capitalize(property), paramClass);
		method.invoke(control, v);
	}

	@Then("editedLeague.$field is \"$value\"")
	public void assertEditedLeague(String field, String value) {
		League editedLeague = LeagueView.getEditedLeague();
		Object v = null;
		if (field.equals("name"))
			v = editedLeague.getName();
		else if (field.equals("description"))
			v = editedLeague.getDescription();
		else if (field.equals("enabled"))
			v = editedLeague.isEnabled();
		else if (field.equals("logoUrl"))
			v = editedLeague.getLogoUrl();
		else if (field.equals("level"))
			v = String.valueOf(editedLeague.getLevel());
		else
			throw new RuntimeException("Unsupported field League." + field);
		assertEquals(v.toString(), value);
	}

	@When("setLogoUrl is invoked with \"$url\"")
	public void setLogoUrl(String url) {
		LeagueView.setLogoUrl(url);
	}
}
