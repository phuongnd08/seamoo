package org.seamoo.cache.memcacheImpl;

import org.seamoo.cache.RemoteCounter;

import com.google.appengine.api.memcache.MemcacheService;

public class MemcacheRemoteCounter implements RemoteCounter {

	private String key;
	private long $default;
	private MemcacheService cacheService;

	public MemcacheRemoteCounter(String key, long $default, MemcacheService cacheService) {
		this.key = key;
		this.$default = $default;
		this.cacheService = cacheService;
	}

	@Override
	public long dec() {
		return alter(-1);
	}

	@Override
	public long inc() {
		return alter(1);
	}

	@Override
	public long alter(long step) {
		return cacheService.increment(key, step, $default);
	}

}
