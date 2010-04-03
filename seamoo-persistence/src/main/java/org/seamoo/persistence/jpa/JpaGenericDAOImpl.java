package org.seamoo.persistence.jpa;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.seamoo.persistence.GenericDAO;

public abstract class JpaGenericDAOImpl<TEntity, TKey> implements
		GenericDAO<TEntity, TKey> {

	EntityManagerFactory emf;
	private Class<TEntity> entityClass;

	@SuppressWarnings("unchecked")
	public JpaGenericDAOImpl(EntityManagerFactory emf) {
		entityClass = (Class<TEntity>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.emf = emf;
	}

	public JpaGenericDAOImpl() {
		this(EMF.get());
	}

	public TEntity findById(TKey key) {
		EntityManager em = emf.createEntityManager();
		return em.find(entityClass, key);
	}

	public List<TEntity> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public TEntity persist(TEntity entity) {
		EntityManager em = emf.createEntityManager();
		em.persist(entity);
		return entity;
	}

	public TEntity[] persist(TEntity[] entities) {
		// TODO Auto-generated method stub
		EntityManager em = emf.createEntityManager();
		for (TEntity e : entities)
			em.persist(e);
		return entities;
	}

	public void delete(TEntity entity) {
		EntityManager em = emf.createEntityManager();
		em.remove(entity);
	}
}
