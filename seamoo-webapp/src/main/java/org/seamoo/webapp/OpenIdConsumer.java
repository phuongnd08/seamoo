package org.seamoo.webapp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openid4java.OpenIDException;
import org.openid4java.discovery.Identifier;

public interface OpenIdConsumer {

	// --- placing the authentication request ---
	public abstract String authRequest(String userSuppliedString, HttpServletRequest httpReq, HttpServletResponse httpResp)
			throws IOException, ServletException;

	// --- processing the authentication response ---
	public abstract Identifier verifyResponse(HttpServletRequest httpReq) throws OpenIDException;

}