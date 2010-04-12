package org.seamoo.webapp.filters;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dyuproject.openid.OpenIdServletFilter.ForwardUriHandler;

public class RedirectUriHandler implements ForwardUriHandler {

	@Override
	public void handle(String forwardUri, HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		// TODO Auto-generated method stub
		System.out.println("request.getReqestURI()=" + request.getRequestURI());
		if (request.getRequestURI().startsWith(forwardUri))
			request.getRequestDispatcher("/app" + forwardUri).forward(request, response);
		//route through the un-rewritten path to avoid filter
		else
			response.sendRedirect(String.format("%s?returnUrl=%s", forwardUri, request.getRequestURI()));
	}

}
