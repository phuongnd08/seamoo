package org.seamoo.utils;

import static org.testng.Assert.*;

import org.seamoo.utils.AliasBuilder;
import org.testng.annotations.Test;

public class EmailExtractorTest {
	@Test
	public void testExtractNameSeveralCases() {
		assertEquals(EmailExtractor.extractName("phuongnd08@saigontech.edu.vn"), "phuongnd08");
		assertEquals(EmailExtractor.extractName(""), "");
	}
}
