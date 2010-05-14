package org.seamoo.daos.twigImpl;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.entities.Member;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.seamoo.test.MockedTimeProvider;
import org.seamoo.utils.TimeProvider;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

public class TwigMemberDaoImplTest extends LocalAppEngineTest {

	TwigMemberDaoImpl memberDao;

	public TwigMemberDaoImplTest() {
	}

	@Override
	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		memberDao = new TwigMemberDaoImpl();
		memberDao.objectDatastoreProvider = new ObjectDatastoreProvider();// create separated odsp to use
		memberDao.timeProvider = new TimeProvider();
	}

	@Override
	@AfterMethod
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@Test
	public void getMemberByOpenIdReturnedMatchedMember() {
		Member m = new Member();
		m.setOpenId("xxx");
		memberDao.persist(m);
		Member m2 = memberDao.findByOpenId("xxx");
		assertEquals(m2.getAutoId(), m.getAutoId());
	}

	@Test
	public void getNonExistentMemberByOpenIdReturnedNull() {
		assertNull(memberDao.findByOpenId("non-existent-open-id"));
	}

	@Test
	public void getNonExistentMemberByKeyReturnedNull() {
		assertNull(memberDao.findByKey(1L));
	}

	@Test
	public void obsoleteMemberByKeyShouldBeRefreshedOnFind() {
		Member m = new Member();
		m.setOpenId("xxx");
		m.setDisplayName("Mr X");
		MockedTimeProvider mtp = new MockedTimeProvider();
		memberDao.timeProvider = mtp;
		mtp.setCurrentTimeStamp(0);
		memberDao.persist(m);

		TwigMemberDaoImpl anotherMemberDao = new TwigMemberDaoImpl();
		anotherMemberDao.objectDatastoreProvider = new ObjectDatastoreProvider();// create separated odsp to use
		// init separate object data store for new dao
		Member reloaded = anotherMemberDao.findByKey(m.getAutoId());
		assertNotSame(reloaded, m);
		reloaded.setDisplayName("Mr Y");
		anotherMemberDao.persist(reloaded);
		assertEquals(memberDao.findByKey(m.getAutoId()).getDisplayName(), "Mr X");
		mtp.setCurrentTimeStamp(TwigMemberDaoImpl.CACHE_PERIOD + 1);
		assertEquals(memberDao.findByKey(m.getAutoId()).getDisplayName(), "Mr Y");
	}

	@Test
	public void obsoleteMemberByOpenIdShouldBeRefreshedOnFind() {
		Member m = new Member();
		m.setOpenId("xxx");
		m.setDisplayName("Mr X");
		MockedTimeProvider mtp = new MockedTimeProvider();
		memberDao.timeProvider = mtp;
		mtp.setCurrentTimeStamp(0);
		memberDao.persist(m);

		TwigMemberDaoImpl anotherMemberDao = new TwigMemberDaoImpl();
		anotherMemberDao.objectDatastoreProvider = new ObjectDatastoreProvider();// create separated odsp to use

		Member reloaded = anotherMemberDao.findByOpenId("xxx");
		reloaded.setDisplayName("Mr Y");
		anotherMemberDao.persist(reloaded);
		assertEquals(memberDao.findByOpenId("xxx").getDisplayName(), "Mr X");
		mtp.setCurrentTimeStamp(TwigMemberDaoImpl.CACHE_PERIOD + 1);
		assertEquals(memberDao.findByOpenId("xxx").getDisplayName(), "Mr Y");
	}

	@Test
	public void nullMemberAlwaysRefreshed() {
		assertNull(memberDao.findByOpenId("xxx"));

		TwigMemberDaoImpl anotherMemberDao = new TwigMemberDaoImpl();
		anotherMemberDao.objectDatastoreProvider = new ObjectDatastoreProvider();// create separated odsp to use

		Member m = new Member();
		m.setOpenId("xxx");
		anotherMemberDao.persist(m);

		assertNotNull(memberDao.findByOpenId("xxx"));

	}

}