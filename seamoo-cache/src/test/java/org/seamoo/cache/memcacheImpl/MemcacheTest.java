package org.seamoo.cache.memcacheImpl;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class MemcacheTest {
	LocalServiceTestHelper helper;

	public MemcacheTest() {
		LocalMemcacheServiceTestConfig memcacheConfig = new LocalMemcacheServiceTestConfig();
		helper = new LocalServiceTestHelper(memcacheConfig);
	}

	/**
	 * 
	 */
	@BeforeMethod
	public void setUp() {
		helper.setUp();
	}

	/**
	 * @see LocalServiceTest#tearDown()
	 */
	@AfterMethod
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void memcacheConcurrentAccess() throws InterruptedException {
		final MemcacheService service = MemcacheServiceFactory.getMemcacheService();
		Runnable runner = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				service.increment("test-key", 1L, 1L);
				try {
					Thread.sleep(200L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				service.increment("test-key", 1L, 1L);
			}
		};

		Thread t1 = new Thread(runner);
		Thread t2 = new Thread(runner);
		t1.start();
		t2.start();
		while (t1.isAlive()) {
			Thread.sleep(100L);
		}
		Assert.assertEquals((Long) (service.get("test-key")), new Long(4L));
	}
}
