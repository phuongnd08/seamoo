package org.seamoo.utils;

public class PatternExtractor {
	public static String getGuidingPattern(String pattern) {
		String base = pattern.replace("[", "").replace("]", "");
		String[] parts = pattern.split("\\[|\\]");
		for (int i = 0; i < parts.length; i++) {
			if (i % 2 == 0) {
				parts[i] = StringBuilder.join(StringBuilder.times("", parts[i].length() + 1), "-");
			}
		}
		String result = StringBuilder.join(parts, "");
		String finalResult = "";
		for (int i = 0; i < result.length(); i++) {
			if (base.charAt(i) == ' ')
				finalResult += ' ';
			else
				finalResult += result.charAt(i);
		}

		return finalResult;
	}
}
