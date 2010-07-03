package org.seamoo.utils;


public class PatternExtractor {
	public static String getGuidingPattern(String pattern) {
		String[] parts = pattern.split("\\[|\\]");
		for (int i = 0; i < parts.length; i++) {
			if (i % 2 == 0) {
				parts[i] = StringBuilder.join(StringBuilder.times("", parts[i].length() + 1), "-");
			}
		}
		return StringBuilder.join(parts, "");
	}
}
