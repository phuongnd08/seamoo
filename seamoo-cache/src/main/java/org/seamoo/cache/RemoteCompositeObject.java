package org.seamoo.cache;

public interface RemoteCompositeObject extends RemoteObject {
	void put(String subKey, Object value);

	<T> T get(Class<T> clazz, String subKey);

	<T> T get(Class<T> clazz, String subKey, T defaultValue);
	
	long inc(String subKey, long delta, long defaultValue);
	
	RemoteObject getSubRemoteObject(String subKey);
}
