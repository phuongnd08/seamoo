package org.seamoo.utils;

import static org.testng.Assert.*;

import org.seamoo.utils.AliasBuilder;
import org.testng.annotations.Test;

public class AliasBuilderTest {
	@Test
	public void testAliasBuilderSeveralCases() {
		assertEquals(AliasBuilder.toAlias(null), "n-a");
		assertEquals(AliasBuilder.toAlias(""), "n-a");
		assertEquals(AliasBuilder.toAlias("ưư"), "xx");
		assertEquals(AliasBuilder.toAlias("Phương Nguyễn 007"), "phxxng-nguyxn-007");
		assertEquals(AliasBuilder.toAlias("@#$%^&*( Phương Nguyễn #$%^&*(007"), "phxxng-nguyxn-007");
	}
}
