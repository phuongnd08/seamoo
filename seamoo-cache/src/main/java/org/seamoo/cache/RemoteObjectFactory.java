package org.seamoo.cache;


public interface RemoteObjectFactory {
	<T> RemoteObject<T> createRemoteObject(Class<T> clazz, String key);

	RemoteCounter createRemoteCounter(String category, String key, long $default);

	RemoteCompositeObject createRemoteCompositeObject(String category, String key);
}
