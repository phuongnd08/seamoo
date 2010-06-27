package org.seamoo.webapp;

import org.openid4java.util.HttpFetcher;

import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;

public class AppEngineGuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(HttpFetcher.class).to(AppEngineHttpFetcher.class).in(Scopes.NO_SCOPE);
	}

	@Provides
	@Singleton
	public URLFetchService providerUrlFetchService() {
		return URLFetchServiceFactory.getURLFetchService();
	}
}