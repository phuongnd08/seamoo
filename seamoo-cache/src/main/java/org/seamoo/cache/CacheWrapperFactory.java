package org.seamoo.cache;

public interface CacheWrapperFactory {
	public <T> CacheWrapper<T> createCacheWrapper(Class<T> clazz, String key);
}
