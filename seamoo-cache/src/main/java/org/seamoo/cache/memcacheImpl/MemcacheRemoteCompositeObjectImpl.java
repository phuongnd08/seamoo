package org.seamoo.cache.memcacheImpl;

import org.seamoo.cache.RemoteCompositeObject;
import org.seamoo.cache.RemoteObject;

import com.google.appengine.api.memcache.MemcacheService;

public class MemcacheRemoteCompositeObjectImpl extends MemcacheRemoteObjectImpl implements RemoteCompositeObject {

	public MemcacheRemoteCompositeObjectImpl(String key, MemcacheService cacheService) {
		super(key, cacheService);
	}

	private String getFullKey(String subKey) {
		return key + "@" + subKey;
	}

	@Override
	public <T> T get(Class<T> clazz, String subKey) {
		T value = (T) cacheService.get(getFullKey(subKey));
		return value;
	}

	@Override
	public <T> T get(Class<T> clazz, String subKey, T defaultValue) {
		T value = get(clazz, subKey);
		return value == null ? defaultValue : value;
	}

	@Override
	public long inc(String subKey, long delta, long defaultValue) {
		return cacheService.increment(getFullKey(subKey), delta, defaultValue);
	}

	@Override
	public void put(String subKey, Object value) {
		cacheService.put(getFullKey(subKey), value);
	}

	@Override
	public RemoteObject getSubRemoteObject(String subKey) {
		// TODO Auto-generated method stub
		return new MemcacheRemoteObjectImpl(getFullKey(subKey), cacheService);
	}

}
