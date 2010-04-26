package org.seamoo.cache.memcacheImpl;

import org.seamoo.cache.CacheWrapper;
import org.seamoo.cache.CacheWrapperFactory;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MemcacheWrapperFactoryImpl implements CacheWrapperFactory {

	private MemcacheService cacheService;

	public MemcacheWrapperFactoryImpl() {
		cacheService = MemcacheServiceFactory.getMemcacheService();
	}

	public <T> CacheWrapper<T> createCacheWrapper(Class<T> clazz, String key) {
		// TODO Auto-generated method stub
		String kindAwareKey = clazz.getCanonicalName() + "@" + key;
		return new MemcacheWrapperImpl<T>(kindAwareKey, cacheService);
	}

}
