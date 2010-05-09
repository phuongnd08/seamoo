package org.seamoo.utils;

public final class AliasBuilder {
	public static String toAlias(String name) {
		String s = name != null ? name.replaceAll("[\\[\\]\\{\\}!@#\\$%\\\\\\/^&*\\<\\>\\(\\)?]", " ") : "";
		s = s.replaceAll("[\\s\\-_]+", "-");
		s = s.replaceAll("[^\\w\\-]", "x");
		if (s.length() >= 1)
			if (s.charAt(s.length() - 1) == '-')
				s = s.substring(0, s.length() - 1);
		if (s.length() >= 1)
			if (s.charAt(0) == '-')
				s = s.substring(1);
		if (s.length() == 0)
			s = "n-a";
		return s.toLowerCase();
	}
}
