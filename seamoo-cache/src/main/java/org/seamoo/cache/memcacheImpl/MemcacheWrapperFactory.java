package org.seamoo.cache.memcacheImpl;

import org.seamoo.cache.CacheWrapper;
import org.seamoo.cache.CacheWrapperFactory;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MemcacheWrapperFactory implements CacheWrapperFactory {

	private MemcacheService cacheService;

	public MemcacheWrapperFactory() {
		cacheService = MemcacheServiceFactory.getMemcacheService();
	}

	public <T> CacheWrapper<T> createCacheWrapper(Class<T> clazz, String key) {
		// TODO Auto-generated method stub
		return new MemcacheWrapper<T>(key, cacheService);
	}

}
