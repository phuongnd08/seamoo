/*
 * Copyright 2010 Vladimir Ritz Bossicard.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Author      : Vladimir Ritz Bossicard
 * Version     : $Revision: 370 $
 * Last edit   : $Date: 2010-03-09 19:14:13 +0100 (Tue, 09 Mar 2010) $
 * Last editor : $Author: vbossica $
 */
package org.workingonit.gwtbridge;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Spring specific <code>RemoteServiceServlet</code> that delegates the
 * execution to a defined Spring bean. Use the {@link GwtServicesRegistrar} to
 * obtain a reference to the bean.
 * 
 * @author Vladimir Ritz Bossicard
 */
public class GwtBridgeServlet extends RemoteServiceServlet {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(GwtBridgeServlet.class);

	public String processCall(String payload) throws SerializationException {
		String url = getThreadLocalRequest().getRequestURI();
		if (logger.isDebugEnabled()) {
			logger.debug("requested url = " + url);
		}

		RemoteService service = findRemoteService(url);

		ServletUtils.setRequest(getThreadLocalRequest());
		ServletUtils.setResponse(getThreadLocalResponse());

		RPCRequest rpcRequest = RPC.decodeRequest(payload, service.getClass(), this);
		onAfterRequestDeserialized(rpcRequest);

		String result = RPC.invokeAndEncodeResponse(service, rpcRequest.getMethod(), rpcRequest.getParameters(),
				rpcRequest.getSerializationPolicy());
		ServletUtils.setRequest(null);
		ServletUtils.setResponse(null);
		return result;
	}

	protected RemoteService findRemoteService(String url) {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		GwtServicesRegistrar registrar = context.getBean(GwtServicesRegistrar.class);
		if (registrar == null) {
			throw new RuntimeException("No bean of class GwtServicesRegistrar defined in the Spring context!");
		}

		String serviceName = url.substring(url.lastIndexOf("/") + 1);
		return registrar.retrieveGwtService(serviceName);
	}

}