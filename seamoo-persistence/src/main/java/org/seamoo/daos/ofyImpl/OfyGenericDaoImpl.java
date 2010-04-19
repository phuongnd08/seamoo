package org.seamoo.daos.ofyImpl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.seamoo.daos.GenericDao;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.vercer.engine.persist.ObjectDatastore;

public abstract class OfyGenericDaoImpl<TEntity, TKey> implements GenericDao<TEntity, TKey> {

	static {
		// ObjectifyService.register()
	}

	Objectify ofy;
	private Class<TEntity> entityClass;

	public OfyGenericDaoImpl() {
		entityClass = (Class<TEntity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		ofy = ObjectifyService.begin();
	}

	public void delete(TEntity entity) {
		// TODO Auto-generated method stub
		ofy.delete(entity);

	}

	public TEntity findByKey(TKey key) {
		try {
			if (key instanceof Long)
				return ofy.get(entityClass, (Long) key);
			if (key instanceof String)
				return ofy.get(entityClass, (String) key);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
		}
		throw new RuntimeException(new IllegalArgumentException("Expected a Long or String key but encounter [" + key + "]"));
	}

	public List<TEntity> getAll() {
		// TODO Auto-generated method stub
		return Lists.newArrayList(ofy.query(entityClass).fetch());
	}

	public TEntity persist(TEntity entity) {
		ofy.put(entity);
		return entity;
	}

	public TEntity[] persist(TEntity[] entities) {
		ofy.put(Lists.newArrayList(entities));
		return entities;
	}
}
