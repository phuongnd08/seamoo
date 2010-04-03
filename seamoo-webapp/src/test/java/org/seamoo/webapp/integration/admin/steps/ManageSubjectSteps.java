package org.seamoo.webapp.integration.admin.steps;

import java.util.List;
import java.util.concurrent.TimeoutException;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.seamoo.webapp.integration.steps.HtmlUnitChecker;
import org.seamoo.webapp.integration.steps.HtmlUnitSteps;
import static org.testng.Assert.*;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ManageSubjectSteps extends HtmlUnitSteps {
	/*
	 * glossary box: control that user can enter information label: control that
	 * display the information of the object
	 */
	HtmlPage page;
	HtmlDivision currentSubjectPanel;

	@Given("I'm in manage subject page")
	public void GotoManageSubject() {
		page = loadPage("/admin/subjects");
	}

	@When("I select the $position subject-panel")
	public void selectSubjectPanel(String position) {
		List<? extends Object> panels = page
				.getByXPath("//div[@class='option-box']");
		Object panel;
		if (position.equals("first")) {
			panel = panels.get(0);
		} else if (position.equals("last")) {
			panel = panels.get(panels.size() - 1);
		} else if (position.equals("n-1")) {
			panel = panels.get(panels.size() - 2);
		} else
			throw new RuntimeException(String.format(
					"Position: \"%s\" is unsupported", position));

		currentSubjectPanel = (HtmlDivision) panel;
	}

	@When("Loading Message is removed")
	public void ensureLoadingMessageRemoved() throws InterruptedException,
			TimeoutException {
		waitForPageLoad(page, new HtmlUnitChecker() {

			@Override
			public boolean isSatisfied() {
				HtmlElement loadingMessage = (HtmlElement) page
						.getElementById("loading-message");
				return loadingMessage == null;
			}
		}, 1000);

	}

	@Then("Button container is $visibleString")
	public void assertBoxButtonContainerVisibility(String visibleString) {
		boolean visible = visibleString.equals("visible") ? true : false;
		HtmlElement buttonContainer = (HtmlElement) currentSubjectPanel
				.getByXPath("tr[@id='buttonContainer']/div[1]").get(0);
		assertElementVisibility(buttonContainer, visible);
		// assertEquals();
	}
}
