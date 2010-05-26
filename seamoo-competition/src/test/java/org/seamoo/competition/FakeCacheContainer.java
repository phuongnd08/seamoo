package org.seamoo.competition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.seamoo.cache.RemoteCompositeObject;
import org.seamoo.cache.RemoteCounter;
import org.seamoo.cache.RemoteObject;
import org.seamoo.cache.RemoteObjectFactory;
import org.seamoo.test.ObjectSerializer;

public class FakeCacheContainer {
	public static class FakeRemoteObject<T> implements RemoteObject<T> {

		protected boolean locked;
		protected String key;
		protected Map<String, Object> map;
		protected Map<String, Lock> lockMap;
		private long putTime;
		private long lockTime;
		private long getTime;

		public FakeRemoteObject(String key, Map<String, Object> map, Map<String, Lock> lockMap, FakeRemoteObjectFactory factory) {
			this(key, map, lockMap);
			this.putTime = factory.putTime;
			this.getTime = factory.getTime;
			this.lockTime = factory.lockTime;
		}

		public FakeRemoteObject(String key, Map<String, Object> map, Map<String, Lock> lockMap) {
			this.key = key;
			this.map = map;
			this.lockMap = lockMap;
		}

		@Override
		public T getObject() {
			// TODO Auto-generated method stub
			System.out.println("getObject(" + key + "): sleep=" + getTime);
			try {
				Thread.sleep(getTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object o = map.get(this.key);
			if (o == null)
				return null;
			try {
				return (T) ObjectSerializer.cloneObject(o);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public boolean tryLock(long timeout) {
			try {
				boolean canLock = lockMap.get(this.key).tryLock(timeout, TimeUnit.MILLISECONDS);
				if (canLock) {
					Thread.sleep(lockTime);
					this.locked = true;
					return true;
				} else
					return false;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}

		@Override
		public void lock(long timeout) throws TimeoutException {
			if (!tryLock(timeout))
				throw new TimeoutException();
			System.out.println("lock(" + key + "): sleep=" + lockTime);
		}

		@Override
		public void putObject(T object) {
			// TODO Auto-generated method stub
			try {
				System.out.println("putObject(" + key + "): sleep=" + putTime);
				Thread.sleep(putTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.map.put(key, object);
		}

		@Override
		public void unlock() {
			// TODO Auto-generated method stub
			System.out.println("unlock(" + key + "): nosleep (would=)" + lockTime);
			lockMap.get(key).unlock();
			this.locked = false;
		}

		@Override
		public boolean isLocked() {
			// TODO Auto-generated method stub
			return locked;
		}

	}

	public static class FakeRemoteCounter implements RemoteCounter {
		String key;
		Map<String, Object> map;
		Map<String, Lock> lockMap;

		public FakeRemoteCounter(String key, Map<String, Object> map, Map<String, Lock> lockMap) {
			this.key = key;
			this.map = map;
			this.lockMap = lockMap;
		}

		@Override
		public long alter(long step) {
			lockMap.get(key).lock();
			Long v = (Long) map.get(key);
			if (v == null)
				v = new Long(step);
			else
				v += step;
			v = Math.max(0L, v);
			map.put(key, v);
			lockMap.get(key).unlock();
			return v;
		}

		@Override
		public long dec() {
			return alter(-1);
		}

		@Override
		public long inc() {
			return alter(+1);
		}

	}

	public static class FakeRemoteCompositeObject extends FakeRemoteObject implements RemoteCompositeObject {

		public FakeRemoteCompositeObject(String key, Map<String, Object> map, Map<String, Lock> lockMap) {
			super(key, map, lockMap);
		}

		private String getFullKey(String subKey) {
			return key + "@" + subKey;
		}

		@Override
		public <T> T get(Class<T> clazz, String subKey) {
			return (T) map.get(getFullKey(subKey));
		}

		@Override
		public <T> T get(Class<T> clazz, String subKey, T defaultValue) {
			T value = (T) map.get(getFullKey(subKey));
			return value == null ? defaultValue : value;
		}

		@Override
		public RemoteObject getSubRemoteObject(String subKey) {
			String fullKey = getFullKey(subKey);
			synchronized (lockMap) {
				if (!lockMap.containsKey(fullKey)) {
					lockMap.put(fullKey, new ReentrantLock());
				}
			}
			return new FakeRemoteObject(fullKey, map, lockMap);
		}

		@Override
		public long inc(String subKey, long delta, long defaultValue) {
			String fullKey = getFullKey(subKey);
			synchronized (lockMap) {
				if (!lockMap.containsKey(fullKey)) {
					lockMap.put(fullKey, new ReentrantLock());
				}
			}
			FakeRemoteCounter counter = new FakeRemoteCounter(fullKey, map, lockMap);
			return counter.alter(delta);
		}

		@Override
		public void put(String subKey, Object value) {
			map.put(getFullKey(subKey), value);
		}

	}

	public static class FakeRemoteObjectFactory implements RemoteObjectFactory {
		Map<String, Object> map;
		Map<String, Lock> lockMap;
		long lockTime = 100;
		long putTime = 1;
		long getTime = 1;

		public FakeRemoteObjectFactory() {
			this.map = new ConcurrentHashMap<String, Object>();
			this.lockMap = new ConcurrentHashMap<String, Lock>();
		}

		@Override
		public synchronized <T> RemoteObject<T> createRemoteObject(Class<T> clazz, String key) {
			// TODO Auto-generated method stub
			String realKey = clazz.getName() + "@" + key;
			synchronized (lockMap) {
				if (!this.lockMap.containsKey(realKey))
					this.lockMap.put(realKey, new ReentrantLock());
			}
			return new FakeRemoteObject<T>(realKey, map, lockMap, this);
		}

		@Override
		public RemoteCounter createRemoteCounter(String category, String key, long $default) {
			String realKey = category + "@" + key;
			synchronized (lockMap) {
				if (!this.lockMap.containsKey(realKey))
					this.lockMap.put(realKey, new ReentrantLock());
			}
			return new FakeRemoteCounter(realKey, map, lockMap);
		}

		@Override
		public RemoteCompositeObject createRemoteCompositeObject(String category, String key) {
			String realKey = category + "@" + key;
			synchronized (lockMap) {
				if (!this.lockMap.containsKey(realKey))
					this.lockMap.put(realKey, new ReentrantLock());
			}
			return new FakeRemoteCompositeObject(realKey, map, lockMap);
		}
	}
}
