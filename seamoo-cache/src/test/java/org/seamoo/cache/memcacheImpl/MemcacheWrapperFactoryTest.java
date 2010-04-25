package org.seamoo.cache.memcacheImpl;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.seamoo.cache.CacheWrapper;
import org.seamoo.cache.CacheWrapperFactory;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * Unit test for simple App.
 */
public class MemcacheWrapperFactoryTest extends LocalAppEngineTest {

	CacheWrapperFactory factory;
	MemcacheService service;
	Map<Object, Long> counters;
	Map<Object, Object> map;

	@Override
	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		factory = new MemcacheWrapperFactory();
		service = MemcacheServiceFactory.getMemcacheService();
		counters = new HashMap<Object, Long>();
		map = new HashMap<Object, Object>();
	}

	@Override
	@AfterMethod
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	private synchronized Long incr(Object key, Long delta, Long init) {
		return service.increment(key, delta, init);
	}

	private synchronized void put(Object key, Object value) {
		service.put(key, value);
	}

	private synchronized Object get(Object key) {
		return service.get(key);
	}

	@Test
	public void getAterPutShouldProduceConsistentObject() {
		CacheWrapperFactory factory = new MemcacheWrapperFactory();
		CacheWrapper<List> cachedList = factory.createCacheWrapper(List.class, "xxx");
		assertNull(cachedList.getObject());
		List<Long> list = new ArrayList<Long>();
		list.add(1L);
		cachedList.putObject(list);
		List<Long> reloadedList = cachedList.getObject();
		assertNotNull(reloadedList);
		assertEquals(reloadedList.size(), 1);
		assertEquals(reloadedList.get(0), new Long(1L));
	}

	@Test
	public void lockShouldBeFine() throws TimeoutException {
		CacheWrapperFactory factory = new MemcacheWrapperFactory();
		CacheWrapper<Object> wrapper1 = factory.createCacheWrapper(Object.class, "wow");
		wrapper1.lock(1000L);
	}

	@Test(expectedExceptions = TimeoutException.class)
	public void lockAfterLockShouldBeTimeout() throws TimeoutException {
		CacheWrapperFactory factory = new MemcacheWrapperFactory();
		CacheWrapper<Object> wrapper1 = factory.createCacheWrapper(Object.class, "wow");
		CacheWrapper<Object> wrapper2 = factory.createCacheWrapper(Object.class, "wow");
		wrapper1.lock(1000L);
		wrapper2.lock(300L);
	}

	@Test
	public void lockingShouldAvoidConcurrentModificationOfObject() throws InterruptedException {
		Runnable runner = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				MemcacheWrapper<List> cachedList = (MemcacheWrapper<List>) factory.createCacheWrapper(List.class, "xxx");
				MemcacheService cacheService = spy(cachedList.cacheService);
				doAnswer(new Answer<Long>() {

					@Override
					public Long answer(InvocationOnMock invocation) throws Throwable {
						// TODO Auto-generated method stub
						Object[] args = invocation.getArguments();
						return incr(args[0], (Long) args[1], (Long) args[2]);
					}

				}).when(cacheService).increment(any(), anyLong(), anyLong());

				doAnswer(new Answer<Object>() {

					@Override
					public Object answer(InvocationOnMock invocation) throws Throwable {
						// TODO Auto-generated method stub
						return get(invocation.getArguments()[0]);
					}
				}).when(cacheService).get(any());

				doAnswer(new Answer() {

					@Override
					public Object answer(InvocationOnMock invocation) throws Throwable {
						// TODO Auto-generated method stub
						put(invocation.getArguments()[0], invocation.getArguments()[1]);
						return null;
					}
				}).when(cacheService).put(any(), any());

				cachedList.cacheService = cacheService;

				try {
					cachedList.lock(1000);
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					return;
				}
				List<Long> list = cachedList.getObject();
				if (list == null)
					list = new ArrayList<Long>();
				list.add(new Long(list.size() + 1));
				try {
					Thread.sleep(300L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cachedList.putObject(list);
				cachedList.unlock();
			}
		};

		Thread t1 = new Thread(runner);
		Thread t2 = new Thread(runner);
		t1.start();
		t2.start();
		while (t1.isAlive() && t2.isAlive()) {
			Thread.sleep(100L);
		}

		CacheWrapper<List> cachedList = factory.createCacheWrapper(List.class, "xxx");
		List<Long> list = cachedList.getObject();
		assertNotNull(list);
		assertEquals(list.size(), 2);
		assertEquals(list.get(0), new Long(1L));
		assertEquals(list.get(1), new Long(2L));
	}
}
