package org.seamoo.daos.twigImpl;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seamoo.daos.GenericDao;
import org.seamoo.entities.training.TrainingEntry;
import org.seamoo.utils.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.common.collect.Lists;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public abstract class TwigGenericDaoImpl<TEntity, TKey> implements GenericDao<TEntity, TKey> {

	protected static interface FieldGetter<T extends Object> {
		public Object getField(T object, String field);

		public Object getKey(T object);
	}

	@Autowired
	TimeProvider timeProvider;
	@Autowired
	protected ObjectDatastoreProvider objectDatastoreProvider = ObjectDatastoreProvider.DEFAULT;
	private Class<TEntity> entityClass;
	private boolean cacheEnabled;
	/**
	 * The cache of entities
	 */
	private Map<String, TEntity> cache;
	/**
	 * The time at which cache item shall be expired
	 */
	private Map<String, Long> expireTime;

	protected final String KEY_FIELD_FOR_CACHE = "$$key";

	private Set<String> cacheFields;

	private FieldGetter<TEntity> fieldGetter;

	private int expiredPeriod;

	public TwigGenericDaoImpl() {
		this(null, null, -1);// default without caching
	}

	public TwigGenericDaoImpl(Set<String> cacheFields, FieldGetter<TEntity> fieldGetter, int expiredPeriod) {
		entityClass = (Class<TEntity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		this.cacheEnabled = cacheFields != null && this.fieldGetter != null;
		if (this.cacheEnabled) {
			cache = new HashMap<String, TEntity>();
			expireTime = new HashMap<String, Long>();
			this.cacheFields = cacheFields;
			this.cacheFields.add(KEY_FIELD_FOR_CACHE);
			this.fieldGetter = fieldGetter;
			this.expiredPeriod = expiredPeriod;
		}
	}

	private String getCacheKey(TEntity entity, String field) {
		Object fieldValue;
		if (!field.equals(KEY_FIELD_FOR_CACHE))
			fieldValue = fieldGetter.getField(entity, field);
		else
			fieldValue = fieldGetter.getKey(entity);
		return field + "@" + fieldValue;

	}

	private String getRawCacheKey(String field, Object fieldValue) {
		return field + "@" + fieldValue;
	}

	protected void putToCache(TEntity entity) {
		long expiredTime = timeProvider.getCurrentTimeStamp() + expiredPeriod;
		for (String field : cacheFields) {
			String key = getCacheKey(entity, field);
			cache.put(key, entity);
			expireTime.put(key, expiredTime);
		}
	}

	protected void removeFromCache(TEntity entity) {
		for (String field : cacheFields) {
			String key = getCacheKey(entity, field);
			cache.remove(key);
		}
	}

	protected TEntity getFromCache(String field, Object fieldValue) {
		String key = getRawCacheKey(field, fieldValue);
		Long time = expireTime.get(key);
		if (time == null || time > timeProvider.getCurrentTimeStamp()) {
			return cache.get(key);
		}
		return null;
	}

	protected ObjectDatastore getOds() {
		return objectDatastoreProvider.getObjectDataStore();
	}

	public void delete(TEntity entity) {
		if (cacheEnabled) {
			removeFromCache(entity);
		}
		getOds().associate(entity);
		getOds().delete(entity);
	}

	public TEntity findByKey(TKey key) {
		if (!cacheEnabled)
			return getOds().load(entityClass, key);
		TEntity entity = getFromCache(KEY_FIELD_FOR_CACHE, key);
		if (entity == null) {
			entity = getOds().load(entityClass, key);
			if (entity != null) {
				putToCache(entity);
			}
		}
		return entity;
	}

	protected TEntity findByFieldWithoutCache(String field, Object fieldValue) {
		RootFindCommand<TEntity> fc = getOds().find().type(entityClass).addFilter(field, FilterOperator.EQUAL, fieldValue);
		if (fc.countResultsNow() == 0)
			return null;
		return fc.returnResultsNow().next();
	}

	protected TEntity findByField(String field, Object fieldValue) {
		if (!cacheEnabled)
			return findByFieldWithoutCache(field, fieldValue);
		TEntity entity = getFromCache(field, fieldValue);
		if (entity == null) {
			entity = findByFieldWithoutCache(field, fieldValue);
			if (entity != null) {
				putToCache(entity);
			}
		}
		return entity;
	}

	public List<TEntity> getAll() {
		// TODO Auto-generated method stub
		return Lists.newArrayList(getOds().find(entityClass));
	}

	public TEntity persist(TEntity entity) {
		getOds().storeOrUpdate(entity);
		getOds().refresh(entity);
		if (cacheEnabled)
			putToCache(entity);
		return entity;
	}

	public TEntity[] persist(TEntity[] entities) {
		if (entities.length != 0) {
			getOds().storeAll(Lists.newArrayList(entities));
			for (TEntity e : entities) {
				getOds().refresh(e);
				if (cacheEnabled)
					putToCache(e);
			}
		}
		return entities;
	}
}
