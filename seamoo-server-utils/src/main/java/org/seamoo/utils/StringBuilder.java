package org.seamoo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.util.StringUtils;

public class StringBuilder {
	public static String urlEncode(String s, String enc) {
		try {
			return URLEncoder.encode(s, enc);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}

	public static String join(String[] s, String delimiter) {
		return StringUtils.arrayToDelimitedString(s, delimiter);
	}

	public static String[] times(String s, int times) {
		String[] result = new String[times];
		for (int i = 0; i < times; i++)
			result[i] = s;
		return result;
	}
}
