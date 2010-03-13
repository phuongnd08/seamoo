package org.seamoo.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {

	private final static PersistenceManagerFactory factory = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	public static PersistenceManagerFactory get() {
		return factory;
	}

}
