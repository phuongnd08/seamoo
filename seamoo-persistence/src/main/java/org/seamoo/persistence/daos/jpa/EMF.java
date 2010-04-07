package org.seamoo.persistence.daos.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
	private static final EntityManagerFactory emfInstance = Persistence
			.createEntityManagerFactory("transactions-optional");

	private EMF() {
	}

	public static EntityManagerFactory get() {
		return emfInstance;
	}

//	public static EntityManager getEM() {
//		return emfInstance.createEntityManager();
//	}
}
