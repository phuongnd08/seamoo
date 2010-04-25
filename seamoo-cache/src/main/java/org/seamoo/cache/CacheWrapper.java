package org.seamoo.cache;

import java.util.concurrent.TimeoutException;

public interface CacheWrapper<T> {
	public void lock(long timeout) throws TimeoutException;
	
	public void unlock();
	
	public T getObject();
	
	public void putObject(T object);
	
}
