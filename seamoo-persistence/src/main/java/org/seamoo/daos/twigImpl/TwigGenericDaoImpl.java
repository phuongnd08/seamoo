package org.seamoo.daos.twigImpl;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seamoo.daos.GenericDao;
import org.seamoo.utils.TimeProvider;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.common.collect.Lists;
import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigGenericDaoImpl<TEntity, TKey> implements GenericDao<TEntity, TKey> {

	protected static interface FieldGetter<T extends Object> {
		public Object getField(T object, String field);

		public Object getKey(T object);
	}

	TimeProvider timeProvider = TimeProvider.DEFAULT;
	ObjectDatastoreProvider objectDatastoreProvider = ObjectDatastoreProvider.DEFAULT;
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
		this.cacheEnabled = cacheFields != null && fieldGetter != null;
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
		if (time != null && time > timeProvider.getCurrentTimeStamp()) {
			return cache.get(key);
		}
		return null;
	}

	protected ObjectDatastore getOds() {
		return objectDatastoreProvider.getObjectDataStore();
	}

	/**
	 * Return eager Ods that will eagerly activate all field of object depend on the depth set
	 * 
	 * @return
	 */
	protected ObjectDatastore getEagerOds() {
		return objectDatastoreProvider.getEagerObjectDatastore();
	}

	public void delete(TEntity entity) {
		if (cacheEnabled) {
			removeFromCache(entity);
		}
		ObjectDatastore ods = getOds();
		ods.associate(entity);
		ods.delete(entity);
	}

	public TEntity findByKey(TKey key) {
		if (!cacheEnabled)
			return findByKeyWithoutCache(key);
		TEntity entity = getFromCache(KEY_FIELD_FOR_CACHE, key);
		if (entity == null) {
			entity = findByKeyWithoutCache(key);
			if (entity != null) {
				putToCache(entity);
			}
		}
		return entity;
	}

	protected TEntity findByKeyWithoutCache(TKey key) {
		ObjectDatastore ods = getOds();
		TEntity entity = ods.load(entityClass, key);
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
		ObjectDatastore ods = getOds();
		ods.storeOrUpdate(entity);
		ods.refresh(entity);
		if (cacheEnabled)
			putToCache(entity);
		return entity;
	}

	public TEntity[] persist(TEntity[] entities) {
		if (entities.length != 0) {
			ObjectDatastore ods = getOds();
			ods.storeAll(Lists.newArrayList(entities));
			for (TEntity e : entities) {
				ods.refresh(e);
				if (cacheEnabled)
					putToCache(e);
			}
		}
		return entities;
	}

	@Override
	public long countAll() {
		return getOds().find().type(entityClass).countResultsNow();
	}

	@Override
	public List<TEntity> getSubSet(long from, int count) {
		RootFindCommand<TEntity> fc = getOds().find().type(entityClass).startFrom((int) from).fetchResultsBy(count);
		return getSegmentedList(fc, count);
	}

	protected List<TEntity> getSegmentedList(RootFindCommand<TEntity> fc, int count) {
		List<TEntity> results = new ArrayList<TEntity>();
		int fetched = 0;
		QueryResultIterator<TEntity> qri = fc.returnResultsNow();
		TEntity entity = null;
		while (qri.hasNext() && fetched < count) {
			entity = qri.next();
			results.add(entity);
			fetched++;
		}
		return results;
	}

	@Override
	public Map<TKey, TEntity> findAllByKeys(Collection<TKey> keys) {
		return getOds().loadAll(entityClass, keys);
	}
}
