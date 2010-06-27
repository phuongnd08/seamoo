package org.seamoo.webapp;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openid4java.OpenIDException;
import org.openid4java.association.AssociationSessionType;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.InMemoryConsumerAssociationStore;
import org.openid4java.consumer.InMemoryNonceVerifier;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.sreg.SRegMessage;
import org.openid4java.message.sreg.SRegRequest;
import org.openid4java.message.sreg.SRegResponse;
import org.seamoo.utils.UrlBuilder;

import com.google.inject.Guice;

public class DefaultOpenIdConsumerImpl implements OpenIdConsumer {

	private ConsumerManager manager;

	public DefaultOpenIdConsumerImpl() {
		manager = Guice.createInjector(new AppEngineGuiceModule()).getInstance(ConsumerManager.class);
		manager.setAssociations(new InMemoryConsumerAssociationStore());
		manager.setNonceVerifier(new InMemoryNonceVerifier(5000));
		manager.setMinAssocSessEnc(AssociationSessionType.DH_SHA256);
	}

	// --- placing the authentication request ---
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.seamoo.webapp.OpenIdConsumer#authRequest(java.lang.String, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public String authRequest(String userSuppliedString, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			// configure the return_to URL where your application will receive
			// the authentication responses from the OpenID provider
			// String returnToUrl = "http://example.com/openid";
			String returnToUrl = UrlBuilder.appendQueryParameter(request.getRequestURL().toString(), request.getQueryString(),
					"is_return=true");

			System.out.println("returnToUrl => " + returnToUrl);

			// perform discovery on the user-supplied identifier
			List discoveries = manager.discover(userSuppliedString);

			// attempt to associate with the OpenID provider
			// and retrieve one service endpoint for authentication
			DiscoveryInformation discovered = manager.associate(discoveries);

			// store the discovery information in the user's session
			request.getSession().setAttribute("openid-disc", discovered);

			// obtain a AuthRequest message to be sent to the OpenID provider
			AuthRequest authRequest = manager.authenticate(discovered, returnToUrl);

			// Simple registration example
			addSimpleRegistrationToAuthRequest(request, authRequest);
			response.sendRedirect(authRequest.getDestinationUrl(true));
			return null;
		} catch (OpenIDException e) {
			// present error to the user
			throw new ServletException(e);
		}

	}

	/**
	 * Simple Registration Extension example.
	 * 
	 * @param request
	 * @param authRequest
	 * @throws MessageException
	 * @see <a href="http://code.google.com/p/openid4java/wiki/SRegHowTo">Simple Registration HowTo</a>
	 * @see <a href="http://openid.net/specs/openid-simple-registration-extension-1_0.html">OpenID Simple Registration Extension
	 *      1.0</a>
	 */
	private void addSimpleRegistrationToAuthRequest(HttpServletRequest request, AuthRequest authRequest) throws MessageException {
		// Attribute Exchange example: fetching the 'email' attribute
		// FetchRequest fetch = FetchRequest.createFetchRequest();
		SRegRequest sregReq = SRegRequest.createFetchRequest();

		String[] attributes = { "email" };
		for (int i = 0, l = attributes.length; i < l; i++) {
			String attribute = attributes[i];
			sregReq.addAttribute(attribute, true);
		}

		authRequest.addExtension(sregReq);
	}

	// --- processing the authentication response ---
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.seamoo.webapp.OpenIdConsumer#verifyResponse(javax.servlet.http.HttpServletRequest)
	 */
	public Identifier verifyResponse(HttpServletRequest request) throws OpenIDException {
		// extract the parameters from the authentication response
		// (which comes in as a HTTP request from the OpenID provider)
		ParameterList response = new ParameterList(request.getParameterMap());

		// retrieve the previously stored discovery information
		DiscoveryInformation discovered = (DiscoveryInformation) request.getSession().getAttribute("openid-disc");

		// extract the receiving URL from the HTTP request
		StringBuffer receivingURL = request.getRequestURL();
		String queryString = request.getQueryString();
		if (queryString != null && queryString.length() > 0)
			receivingURL.append("?").append(request.getQueryString());

		// verify the response; ConsumerManager needs to be the same
		// (static) instance used to place the authentication request
		VerificationResult verification = manager.verify(receivingURL.toString(), response, discovered);

		// examine the verification result and extract the verified
		// identifier
		Identifier verified = verification.getVerifiedId();
		if (verified != null) {
			AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();

			receiveSimpleRegistration(request, authSuccess);
			return verified; // success
		}

		return null;
	}

	/**
	 * @param request
	 * @param authSuccess
	 * @throws MessageException
	 */
	private void receiveSimpleRegistration(HttpServletRequest request, AuthSuccess authSuccess) throws MessageException {
		if (authSuccess.hasExtension(SRegMessage.OPENID_NS_SREG)) {
			MessageExtension ext = authSuccess.getExtension(SRegMessage.OPENID_NS_SREG);
			if (ext instanceof SRegResponse) {
				SRegResponse sregResp = (SRegResponse) ext;
				for (Iterator iter = sregResp.getAttributeNames().iterator(); iter.hasNext();) {
					String name = (String) iter.next();
					String value = sregResp.getParameterValue(name);
					request.setAttribute(name, value);
				}
			}
		}
	}

}
