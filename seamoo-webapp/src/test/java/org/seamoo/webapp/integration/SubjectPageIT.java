package org.seamoo.webapp.integration;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SubjectPageIT extends ITBase {

	@Test
	public void subjectPageShouldContainEnglishSubject() {
		HtmlPage page = loadGETWebResponse("/subjects/");
		HtmlDivision englishBox = (HtmlDivision) page.getByXPath(
				"(//div[@class='option-box'])[1]").get(0);
		HtmlAnchor subjectLink = (HtmlAnchor) englishBox.getByXPath(".//div/a")
				.get(0);
		// HtmlAnchor subjectLink = (HtmlAnchor) page.getByXPath(
		// "(//div[@class='option-box'])[1]//div/a").get(0);
		assertEquals(subjectLink.getTextContent(), "English");
	}
}
