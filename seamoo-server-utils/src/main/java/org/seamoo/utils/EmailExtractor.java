package org.seamoo.utils;

public final class EmailExtractor {
	public static String extractName(String email) {
		int i = email.indexOf('@');
		if (i != -1)
			return email.substring(0, i);
		return "";
	}

}
