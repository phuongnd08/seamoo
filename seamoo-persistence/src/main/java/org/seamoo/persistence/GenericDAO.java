package org.seamoo.persistence;

import java.util.List;

public interface GenericDAO<TEntity, TKey> {
	public List<TEntity> getAll();
	public TEntity findById(TKey key);
	public TEntity persist(TEntity entity);
	public TEntity[] persist(TEntity[] entities);
	public void delete(TEntity entity);
}
