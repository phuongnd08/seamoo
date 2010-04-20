package org.seamoo.webapp.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dyuproject.openid.OpenIdServletFilter.ForwardUriHandler;

public class RedirectUriHandler implements ForwardUriHandler {

	public static String getEncodedRedirectUrl(HttpServletRequest request) {
		String requestUrl = request.getRequestURI();
		if (request.getQueryString() != null)
			requestUrl += "?" + request.getQueryString();
		System.out.println("request.getReqestURI()=" + requestUrl);
		try {
			return URLEncoder.encode(requestUrl, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}

	@Override
	public void handle(String forwardUri, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		// TODO Auto-generated method stub
		if (request.getRequestURI().startsWith(forwardUri))
			request.getRequestDispatcher("/app" + forwardUri).forward(request, response);
		// route through the un-rewritten path to avoid filter
		else
			response.sendRedirect(String.format("%s?returnUrl=%s", forwardUri, getEncodedRedirectUrl(request)));
	}

}
