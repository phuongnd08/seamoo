package org.seamoo.cache.memcacheImpl;

import org.seamoo.cache.RemoteCompositeObject;
import org.seamoo.cache.RemoteCounter;
import org.seamoo.cache.RemoteObject;
import org.seamoo.cache.RemoteObjectFactory;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class MemcacheRemoteObjectFactoryImpl implements RemoteObjectFactory {

	private MemcacheService cacheService;

	public MemcacheRemoteObjectFactoryImpl() {
		cacheService = MemcacheServiceFactory.getMemcacheService();
	}

	public <T> RemoteObject<T> createRemoteObject(Class<T> clazz, String key) {
		String kindAwareKey = clazz.getCanonicalName() + "@" + key;
		return new MemcacheRemoteObjectImpl<T>(kindAwareKey, cacheService);
	}

	@Override
	public RemoteCounter createRemoteCounter(String category, String key, long $default) {
		String categoryAwareKey = category + "@" + key;
		return new MemcacheRemoteCounter(categoryAwareKey, $default, cacheService);
	}

	@Override
	public RemoteCompositeObject createRemoteCompositeObject(String category, String key) {
		String categoryAwareKey = category + "@" + key;
		return new MemcacheRemoteCompositeObjectImpl(categoryAwareKey, cacheService);
	}

}
