package org.seamoo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.util.StringUtils;

public class UrlBuilder {

	public static String getEncodedUrl(String uri, String queryString) {
		String requestUrl = uri;
		if (queryString != null)
			requestUrl += "?" + queryString;
		System.out.println("requestUrl=" + requestUrl);
		try {
			return URLEncoder.encode(requestUrl, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	public static String getQueryString(List<UrlParameter> pairList) {
		String[] s = new String[pairList.size()];
		for (int i = 0; i < s.length; i++)
			s[i] = pairList.get(i).toString();
		return StringUtils.arrayToDelimitedString(s, "&");
	}

	public static String getQueryUrl(String baseUrl, String queryString) {
		if (queryString != null && !queryString.equals(""))
			return String.format("%s?%s", baseUrl, queryString);
		return baseUrl;
	}

	public static String getQueryUrl(String baseUrl, List<UrlParameter> params) {
		if (params.size() == 0)
			return baseUrl;
		return String.format("%s?%s", baseUrl, getQueryString(params));
	}

	public static String appendQueryParameter(String baseUrl, String queryString, String extQueryString) {
		if (queryString != null && !queryString.equals("")) {
			return String.format("%s?%s&%s", baseUrl, queryString, extQueryString);
		} else
			return String.format("%s?%s", baseUrl, extQueryString);
	}

	public static String appendQueryParameter(String baseUrl, String queryString, UrlParameter extParameter) {
		return appendQueryParameter(baseUrl, queryString, extParameter.toString());
	}

}
