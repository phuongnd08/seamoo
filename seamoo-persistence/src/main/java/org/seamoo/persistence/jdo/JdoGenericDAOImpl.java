package org.seamoo.persistence.jdo;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.seamoo.persistence.GenericDAO;

public abstract class JdoGenericDAOImpl<TEntity, TKey> implements
		GenericDAO<TEntity, TKey> {

	private PersistenceManagerFactory pmf;

	protected PersistenceManager getPM() {
		return pmf.getPersistenceManager();
	}

	private Class<TEntity> entityClass;

	@SuppressWarnings("unchecked")
	public JdoGenericDAOImpl(PersistenceManagerFactory emf) {
		entityClass = (Class<TEntity>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.pmf = emf;
	}

	public JdoGenericDAOImpl() {
		this(PMF.get());
	}

	public TEntity findById(TKey key) {
		PersistenceManager pm = pmf.getPersistenceManager();
		return pm.getObjectById(entityClass, key);
	}

	public List<TEntity> getAll() {
		PersistenceManager pm = pmf.getPersistenceManager();
		Query query = pm.newQuery(entityClass);
		return (List<TEntity>) query.execute();
	}

	public void persist(TEntity entity) {
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			pm.makePersistent(entity);
		} finally {
			pm.close();
		}
	}

	public void persist(TEntity[] entities) {
		// TODO Auto-generated method stub
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			pm.makePersistentAll(entities);
		} finally {
			pm.close();
		}

	}
}
