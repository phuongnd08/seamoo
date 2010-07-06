package org.seamoo.utils;

import static org.testng.Assert.*;

import org.seamoo.utils.AliasBuilder;
import org.testng.annotations.Test;

public class PatternExtractorTest {
	@Test
	public void testGetGuidingPattern() {
		assertEquals(PatternExtractor.getGuidingPattern("ab[x]cd"), "--x--");
		assertEquals(PatternExtractor.getGuidingPattern("a[u]b[xy]cd"), "-u-xy--");
		assertEquals(PatternExtractor.getGuidingPattern("[a]xbcd"), "a----");
		assertEquals(PatternExtractor.getGuidingPattern("[a]x bcd"), "a- ---");
	}
}
