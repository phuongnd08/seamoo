package org.seamoo.persistence;

import java.util.List;

public interface GenericDAO<TEntity, TKey> {
	public List<TEntity> getAll();
	public TEntity findById(TKey key);
	public void persist(TEntity entity);
	public void persist(TEntity[] entities);
	//public void persist(List<Entity> entities);
}
