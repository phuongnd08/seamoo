package org.seamoo.daos;

import java.util.List;

public interface GenericDao<TEntity, TKey> {
	public List<TEntity> getAll();
	public TEntity findByKey(TKey key);
	public TEntity persist(TEntity entity);
	public TEntity[] persist(TEntity[] entities);
	public void delete(TEntity entity);
}