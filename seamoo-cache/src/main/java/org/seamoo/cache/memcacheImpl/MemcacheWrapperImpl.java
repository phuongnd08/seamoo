package org.seamoo.cache.memcacheImpl;

import java.util.concurrent.TimeoutException;

import org.seamoo.cache.CacheWrapper;

import com.google.appengine.api.memcache.MemcacheService;

public class MemcacheWrapperImpl<T> implements CacheWrapper<T> {

	private String key;
	private String keyLock;
	MemcacheService cacheService;
	private boolean locked;

	public MemcacheWrapperImpl(String key, MemcacheService cacheService) {
		this.key = key;
		this.keyLock = key + "@lock";
		this.cacheService = cacheService;
		this.locked = false;
	}

	public T getObject() {
		// TODO Auto-generated method stub
		return (T) cacheService.get(key);
	}

	public void lock(long timeout) throws TimeoutException {
		long v = cacheService.increment(this.keyLock, 1L, 0L);
		long sleepTime = 0;
		while (v != 1 && sleepTime < timeout) {
			cacheService.increment(this.keyLock, -1L);
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			sleepTime += 100L;
			v = cacheService.increment(this.keyLock, 1L);
		}
		if (v != 1) {
			cacheService.increment(this.keyLock, -1L);
			throw new TimeoutException();
		}
		this.locked = true;
	}

	public void putObject(T object) {
		// TODO Auto-generated method stub
		cacheService.put(key, object);
	}

	public void unlock() {
		// TODO Auto-generated method stub
		if (this.locked) {
			long v = cacheService.increment(this.keyLock, -1L, 1L);
			if (v != 0L) {
				throw new IllegalStateException("Unlock not properly restore lock key");
			}
			this.locked = false;
		} else
			throw new IllegalStateException("Lock has not been acquired");
	}

	@Override
	public void resetLock() {
		cacheService.put(keyLock, 0);
	}

}
