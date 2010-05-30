package org.seamoo.daos;

import java.util.List;
import java.util.Map;

public interface GenericDao<TEntity, TKey> {
	List<TEntity> getAll();

	TEntity findByKey(TKey key);

	Map<TKey, TEntity> findAllByKeys(List<TKey> keys);

	TEntity persist(TEntity entity);

	TEntity[] persist(TEntity[] entities);

	void delete(TEntity entity);

	long countAll();

	List<TEntity> getSubSet(long from, int count);
}
