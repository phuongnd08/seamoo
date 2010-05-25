package org.seamoo.cache.memcacheImpl;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.seamoo.cache.RemoteObject;
import org.seamoo.cache.RemoteObjectFactory;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * Unit test for simple App.
 */
public class MemcacheWrapperFactoryImplTest extends LocalAppEngineTest {

	RemoteObjectFactory factory;

	@Override
	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		factory = new MemcacheRemoteObjectFactoryImpl();
	}

	@Override
	@AfterMethod
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@Test
	public void getAterPutShouldProduceConsistentObject() {
		RemoteObjectFactory factory = new MemcacheRemoteObjectFactoryImpl();
		RemoteObject<List> cachedList = factory.createRemoteObject(List.class, "xxx");
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
	public void firstLockShouldBeFine() throws TimeoutException {
		RemoteObjectFactory factory = new MemcacheRemoteObjectFactoryImpl();
		RemoteObject<Object> wrapper1 = factory.createRemoteObject(Object.class, "wow");
		wrapper1.lock(1000L);
	}

	@Test(expectedExceptions = TimeoutException.class)
	public void lockAfterLockShouldBeTimeout() throws TimeoutException {
		RemoteObjectFactory factory = new MemcacheRemoteObjectFactoryImpl();
		RemoteObject<Object> wrapper1 = factory.createRemoteObject(Object.class, "wow");
		RemoteObject<Object> wrapper2 = factory.createRemoteObject(Object.class, "wow");
		wrapper1.lock(1000L);
		wrapper2.lock(300L);
	}

	@Test
	public void lockAfterUnlockShouldBeFine() throws TimeoutException {
		RemoteObjectFactory factory = new MemcacheRemoteObjectFactoryImpl();
		RemoteObject<Object> wrapper1 = factory.createRemoteObject(Object.class, "wow");
		RemoteObject<Object> wrapper2 = factory.createRemoteObject(Object.class, "wow");
		wrapper1.lock(1000L);
		wrapper1.unlock();
		wrapper2.lock(300L);
	}

	@Test(expectedExceptions = { IllegalStateException.class })
	public void unlockTwiceShouldThrowInvalidOperationException() throws TimeoutException {
		RemoteObjectFactory factory = new MemcacheRemoteObjectFactoryImpl();
		RemoteObject<Object> wrapper1 = factory.createRemoteObject(Object.class, "wow");
		wrapper1.lock(1000L);
		wrapper1.unlock();
		wrapper1.unlock();
	}

	@Test(expectedExceptions = { IllegalStateException.class })
	public void unlockWithoutLockShouldThrowInvalidOperationException() throws TimeoutException {
		RemoteObjectFactory factory = new MemcacheRemoteObjectFactoryImpl();
		RemoteObject<Object> wrapper1 = factory.createRemoteObject(Object.class, "wow");
		wrapper1.unlock();
	}

	public static class T1 implements Serializable {
		String t1Field;
	}

	public static class T2 implements Serializable {
		String t2Field;
	}

	@Test
	public void differentKindsWithSameKeyShouldReturnDifferentValues() {
		RemoteObjectFactory factory = new MemcacheRemoteObjectFactoryImpl();
		RemoteObject<T1> wrapper1 = factory.createRemoteObject(T1.class, "wow");
		T1 t1 = new T1();
		t1.t1Field = "Hello";
		wrapper1.putObject(t1);
		RemoteObject<T2> wrapper2 = factory.createRemoteObject(T2.class, "wow");
		T2 t2 = new T2();
		t2.t2Field = "Bonjure";
		wrapper2.putObject(t2);
		assertEquals("Hello", wrapper1.getObject().t1Field);
		assertEquals("Bonjure", wrapper2.getObject().t2Field);
	}

	@Test
	public void failedUnlockShouldNotContaminateCache() throws TimeoutException {
		RemoteObjectFactory factory = new MemcacheRemoteObjectFactoryImpl();
		RemoteObject<T1> wrapper1 = factory.createRemoteObject(T1.class, "wow");
		RemoteObject<T1> wrapper2 = factory.createRemoteObject(T1.class, "wow");
		wrapper1.lock(1000L);
		try {
			wrapper2.lock(100L);
		} catch (TimeoutException e) {

		}

		wrapper1.unlock();
		wrapper2.lock(100L);

	}
}
