package org.seamoo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StringBuilder {
	public static String urlEncode(String s, String enc) {
		try {
			return URLEncoder.encode(s, enc);
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}
}
