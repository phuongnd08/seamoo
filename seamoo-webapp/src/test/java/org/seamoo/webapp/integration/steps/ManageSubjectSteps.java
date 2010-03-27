package org.seamoo.webapp.integration.steps;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ManageSubjectSteps extends HtmlUnitSteps {
	/*	glossary
	 * 	box: control that user can enter information
	 * 	label: control that display the information of the object
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

	@Then("$controlId label is \"$text\"")
	public void assertLabelText(String controlId, String text) {
		HtmlElement label = getCurrentSubjecPanelElementById(controlId
				+ "_label");
		assertEquals(label.getTextContent(), text);
	}

	private HtmlElement getCurrentSubjecPanelElementById(String controlId) {
		String xpath = String.format(".//*[@id='%s']", controlId);
		return (HtmlElement) currentSubjectPanel.getByXPath(xpath).get(0);
	}

	@When("I fill $controlId box with \"$value\"")
	public void fillBox(String controlId, String text) {
		HtmlElement element = getCurrentSubjecPanelElementById(controlId
				+ "_box");
		if (element.getNodeName().equalsIgnoreCase("input"))
			element.setAttribute("value", text);
		else
			element.setTextContent(text);
	}

	@When("I $verb the enabled checkbox")
	public void setEnabled(String verb) {
		HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) getCurrentSubjecPanelElementById("enabled_chk");
		checkbox.setChecked(verb.equals("check"));
	}

	@When("I click $button button")
	public void clickButton(String button) throws IOException {
		HtmlElement element = (HtmlElement) getCurrentSubjecPanelElementById(button
				+ "-btn");
		if (element.getNodeName().equals("a"))
			((HtmlAnchor) element).click();
		else if (element.getNodeName().equals("button"))
			((HtmlButton) element).click();
		else
			((HtmlButtonInput) element).click();
	}

	@Then("The $controlId box is $visibleString")
	public void assertBoxControlVisibility(String controlId, String visibleString) {
		boolean visible = visibleString.equals("visible") ? true : false;
		HtmlElement element = getCurrentSubjecPanelElementById(controlId+"_box");
		assertElementVisibility(element, visible);
		// assertEquals();
	}
}
