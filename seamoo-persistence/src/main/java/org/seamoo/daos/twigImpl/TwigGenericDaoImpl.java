package org.seamoo.daos.twigImpl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.seamoo.daos.GenericDao;

import com.google.common.collect.Lists;
import com.vercer.engine.persist.ObjectDatastore;

public abstract class TwigGenericDaoImpl<TEntity, TKey> implements GenericDao<TEntity, TKey> {

	private Class<TEntity> entityClass;

	public TwigGenericDaoImpl() {
		entityClass = (Class<TEntity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected ObjectDatastore getOds() {
		return TOD.getObjectDataStore();
	}

	public void delete(TEntity entity) {
		// TODO Auto-generated method stub
		getOds().associate(entity);
		getOds().delete(entity);

	}

	public TEntity findByKey(TKey key) {
		return getOds().load(entityClass, key);
	}

	public List<TEntity> getAll() {
		// TODO Auto-generated method stub
		return Lists.newArrayList(getOds().find(entityClass));
	}

	public TEntity persist(TEntity entity) {
		getOds().storeOrUpdate(entity);
		getOds().refresh(entity);
		return entity;
	}

	public TEntity[] persist(TEntity[] entities) {
		if (entities.length != 0) {
			getOds().storeAll(Lists.newArrayList(entities));
			for (TEntity e : entities) {
				getOds().refresh(e);
			}
		}
		return entities;
	}
}
