package org.seamoo.webapp.integration.admin;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.seamoo.webapp.integration.shared.HtmlUnitSteps;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class SiteSettingsSteps extends HtmlUnitSteps {
	HtmlPage page;

	@Given("Site is in $mode mode")
	public void SetSiteMode(String mode) throws IOException {
		// Submit a request to change the operating mode of the site
		WebRequestSettings settings = new WebRequestSettings(new URL(
				getServerBase() + "/admin/update-site-settings"),
				HttpMethod.POST);

		settings.setRequestParameters(Arrays
				.asList(new NameValuePair[] { new NameValuePair(
						"operatingMode", mode) }));
		getWebClient().loadWebResponse(settings);
	}

	@Given("I'm in site settings page")
	public void LoadSiteSettingsPage() {
		page = loadPage("/admin/site-settings");
	}

	@Then("current mode indicator is \"$indicator\"")
	public void assertCurrentModeIndicator(String indicator) {
		HtmlElement modeIndicator = (HtmlElement) page.getByXPath(
				"//div[@id='content']/div[1]/p[1]/strong").get(0);
		assertEquals(modeIndicator.getTextContent(), indicator);
	}

	@Then("current switch button is \"$buttonCaption\"")
	public void assertCurrentSwitchButton(String buttonCaption)
			throws IOException {
		HtmlButton changeButton = (HtmlButton) page.getByXPath(
				"//div[@id='content']/div[1]/form[1]/p[1]/button[1]").get(0);
		assertEquals(changeButton.getTextContent(), buttonCaption);
	}

	@When("I click on switch button")
	public void clickSwitchButton() throws IOException {
		HtmlButton changeButton = (HtmlButton) page.getByXPath(
				"//div[@id='content']/div[1]/form[1]/p[1]/button[1]").get(0);
		page = (HtmlPage) changeButton.click();
	}
}
