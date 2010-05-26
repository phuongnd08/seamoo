package org.seamoo.cache.memcacheImpl;

import java.util.concurrent.TimeoutException;

import org.seamoo.cache.RemoteObject;

import com.google.appengine.api.memcache.MemcacheService;

public class MemcacheRemoteObjectImpl<T> implements RemoteObject<T> {

	protected String key;
	protected String keyLock;
	protected MemcacheService cacheService;
	private boolean locked;

	public MemcacheRemoteObjectImpl(String key, MemcacheService cacheService) {
		this.key = key;
		this.keyLock = key + "@lock";
		this.cacheService = cacheService;
		this.locked = false;
	}

	public T getObject() {
		// TODO Auto-generated method stub
		return (T) cacheService.get(key);
	}

	@Override
	public boolean tryLock(long timeout) {
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
			return false;
		}
		this.locked = true;
		return true;
	}

	public void lock(long timeout) throws TimeoutException {
		if (!tryLock(timeout))
			throw new TimeoutException();
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
	public boolean isLocked() {
		Long value = (Long) cacheService.get(this.keyLock);
		if (value == null)
			return false;
		return value > 0;
		//we may use value==1 here, however, there will be situation when the isLocked executed in the middle
		//of a tryLock, in which the operation try to acquire a lock over a locked object, thus, the value may > 1.
		//In this situation, certainly the object remains locked
	}
}
