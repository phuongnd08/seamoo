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

	protected ObjectDatastore getOds() {
		return ods;
	}

	public void delete(TEntity entity) {
		// TODO Auto-generated method stub
		ods.associate(entity);
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
		ods.storeOrUpdate(entity);
		ods.refresh(entity);
		return entity;
	}

	public TEntity[] persist(TEntity[] entities) {
		if (entities.length != 0) {
			ods.storeAll(Lists.newArrayList(entities));
			for (TEntity e : entities) {
				ods.refresh(e);
			}
		}
		return entities;
	}
}
