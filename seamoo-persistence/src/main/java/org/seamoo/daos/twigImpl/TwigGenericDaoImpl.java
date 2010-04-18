package org.seamoo.daos.twigImpl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.seamoo.daos.GenericDao;

import com.google.common.collect.Lists;
import com.vercer.engine.persist.ObjectDatastore;

public abstract class TwigGenericDaoImpl<TEntity, TKey> implements GenericDao<TEntity, TKey> {

	ObjectDatastore ods;
	private Class<TEntity> entityClass;

	public TwigGenericDaoImpl() {
		entityClass = (Class<TEntity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		ods = TOD.getObjectDataStore();
	}

	public void delete(TEntity entity) {
		// TODO Auto-generated method stub
		ods.delete(entity);

	}

	public TEntity findByKey(TKey key) {
		return ods.load(entityClass, key);
	}

	public List<TEntity> getAll() {
		// TODO Auto-generated method stub
		return Lists.newArrayList(ods.find(entityClass));
	}

	public TEntity persist(TEntity entity) {
		ods.store(entity);
		return entity;
	}

	public TEntity[] persist(TEntity[] entities) {
		ods.storeAll(Lists.newArrayList(entities));
		return entities;
	}
}
