package org.seamoo.utils;

import static org.testng.Assert.*;

import org.seamoo.utils.AliasBuilder;
import org.testng.annotations.Test;

public class HashBuilderTest {
	@Test
	public void testMD5Hash() {
		assertEquals(HashBuilder.getMD5Hash("iHaveAn@email.com"), "ddf097499b3763483d3acf39246a740f");
	}
}
