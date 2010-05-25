package org.seamoo.cache;

import java.util.Map;
import java.util.Queue;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A very simple cache using java.util.concurrent.
 * <p/>
 * User: treeder Date: Mar 15, 2009 Time: 8:42:01 PM
 */
public class KittyLocalCache implements LocalCache {

	public int DEFAULT_STORE_SECONDS = Integer.MAX_VALUE;

	private Map<String, Object[]> cache;
	/**
	 * Used to restrict the size of the cache map.
	 */
	private Queue<String> queue;
	private int maxSize;
	/**
	 * Using this integer because ConcurrentLinkedQueue.size is not constant time.
	 */
	private AtomicInteger size = new AtomicInteger();

	public KittyLocalCache(int maxSize) {
		this.maxSize = maxSize;
		cache = new ConcurrentHashMap<String, Object[]>(maxSize);
		queue = new ConcurrentLinkedQueue<String>();
	}

	public void put(String key, Object val) {
		put(key, val, null);
	}

	public void put(String key, Object val, Integer secondsToLive) {
		if (key == null)
			throw new RuntimeException("Key cannot be null!");
		secondsToLive = secondsToLive != null ? secondsToLive : DEFAULT_STORE_SECONDS;
		cache.put(key, new Object[] { System.currentTimeMillis() + secondsToLive, val });
		queue.add(key);
		size.incrementAndGet();

		while (size.get() > maxSize && maxSize > 0) {
			Object toRemove = queue.poll();
			if (toRemove == null)
				break;
			if (toRemove != null) {
				remove(key);
			}
		}
	}

	public Object get(String key) {
		if (cache.containsKey(key)) {
			Long expires = (Long) cache.get(key)[0];
			if (expires - System.currentTimeMillis() > 0) {
				return cache.get(key)[1];
			} else {
				remove(key);
			}
		}
		return null;
	}

	/**
	 * Returns boolean to stay compatible with ehcache and memcached.
	 * 
	 * @see #removeAndGet for alternate version.
	 * 
	 */
	public boolean remove(Object key) {
		return removeAndGet(key) != null;
	}

	public Object removeAndGet(Object key) {
		Object[] entry = cache.remove(key);
		if (entry != null) {
			return entry[1];
		}
		size.decrementAndGet();
		return null;
	}

	public int size() {
		return size.get();
	}

	public Map<String, Object> getAll(Collection<String> keysCollection) {
		Map<String, Object> ret = new HashMap<String, Object>();
		for (String o : keysCollection) {
			ret.put(o, cache.get(o));
		}
		return ret;
	}

	public void clear() {
		cache.clear();
	}

	public int mapSize() {
		return cache.size();
	}

	public int queueSize() {
		return queue.size();
	}
}