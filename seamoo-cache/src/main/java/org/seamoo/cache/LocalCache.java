package org.seamoo.cache;

import java.util.Collection;
import java.util.Map;

public interface LocalCache {
	void put(String key, Object value, Integer secondsToLive);

	Object get(String key);

	Map<String, Object> getAll(Collection<String> keysCollection);
}
