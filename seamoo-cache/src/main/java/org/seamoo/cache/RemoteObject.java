package org.seamoo.cache;

import java.util.concurrent.TimeoutException;

public interface RemoteObject<T> {
	void lock(long timeout) throws TimeoutException;

	boolean tryLock(long timeout);

	void unlock();

	void resetLock();

	boolean isLocked();

	T getObject();

	void putObject(T object);

}
