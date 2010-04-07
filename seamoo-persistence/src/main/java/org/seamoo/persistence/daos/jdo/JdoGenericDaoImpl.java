package org.seamoo.persistence.daos.jdo;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.seamoo.persistence.daos.GenericDao;

public abstract class JdoGenericDaoImpl<TEntity, TKey> implements GenericDao<TEntity, TKey> {

	private PersistenceManagerFactory pmf;

	protected PersistenceManager getPM() {
		return pmf.getPersistenceManager();
	}

	private Class<TEntity> entityClass;

	@SuppressWarnings("unchecked")
	public JdoGenericDaoImpl(PersistenceManagerFactory emf) {
		entityClass = (Class<TEntity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.pmf = emf;
	}

	public JdoGenericDaoImpl() {
		this(PMF.get());
	}

	public TEntity findById(TKey key) {
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			return pm.getObjectById(entityClass, key);
		} catch (JDOObjectNotFoundException ex) {
			return null;
		}
	}

	public List<TEntity> getAll() {
		PersistenceManager pm = pmf.getPersistenceManager();
		Query query = pm.newQuery(entityClass);
		return (List<TEntity>) query.execute();
	}

	public TEntity persist(TEntity entity) {
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			pm.makePersistent(entity);
		} finally {
			pm.close();
		}
		return entity;
	}

	public TEntity[] persist(TEntity[] entities) {
		// TODO Auto-generated method stub
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			pm.makePersistentAll(entities);
		} finally {
			pm.close();
		}
		return entities;

	}

	public void delete(TEntity entity) {
		PersistenceManager pm = pmf.getPersistenceManager();
		try {
			TKey key = (TKey) JdoPersistenceHelper.findPrimaryKey(entity);
			pm.deletePersistent(pm.getObjectById(entityClass, key));
		} finally {
			pm.close();
		}
	}

}
