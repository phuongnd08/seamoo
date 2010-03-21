package org.seamoo.webapp.integration;

import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class SubjectPageIT extends ITBase {

	@Test
	public void subjectPageShouldContainEnglishSubject() {
		HtmlPage page = loadGETWebResponse("/subjects/");
		throw new RuntimeException();
	}

	@Test
	public void indexPageShouldBeTheSameAsSubjectsPage() {
		throw new RuntimeException();
	}
}
