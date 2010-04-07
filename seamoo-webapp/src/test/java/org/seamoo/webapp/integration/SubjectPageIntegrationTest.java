package org.seamoo.webapp.integration;

import static org.testng.Assert.assertEquals;

import org.seamoo.webapp.integration.shared.HtmlUnitSteps;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SubjectPageIntegrationTest extends HtmlUnitSteps {

	@Test
	public void subjectListPageShouldContainEnglishSubject() {
		HtmlPage page = loadPage("/subjects/");
		HtmlDivision englishBox = (HtmlDivision) page.getByXPath(
				"(//div[@class='option-box'])[1]").get(0);
		HtmlAnchor subjectLink = (HtmlAnchor) englishBox.getByXPath(".//div/a")
				.get(0);
		// HtmlAnchor subjectLink = (HtmlAnchor) page.getByXPath(
		// "(//div[@class='option-box'])[1]//div/a").get(0);
		assertEquals(subjectLink.getTextContent(), "English");
	}
	
	@Test
	public void englishSubjectPageShouldContainLinkToAmateurLeague(){
		HtmlPage page = loadPage("/subjects/1-english");
		HtmlDivision amateurBox = (HtmlDivision) page.getByXPath("(//div[@id='content']//div[@class='description-box'])[1]").get(0);
		HtmlAnchor amateurLink = (HtmlAnchor) amateurBox.getByXPath(".//div/a").get(0);
		assertEquals(amateurLink.getTextContent(), "Giải nghiệp dư");
	}
}
