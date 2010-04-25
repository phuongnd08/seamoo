package org.seamoo.daos.twigImpl;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.seamoo.daos.MemberDao;
import org.seamoo.entities.Member;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.IObjectFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;

import com.vercer.engine.persist.ObjectDatastore;
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
		// init separate object data store for new dao
		memberDao.ods = new AnnotationObjectDatastore();
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
	public void obsoleteMemberByKeyShouldBeRefreshed() {
		Member m = new Member();
		m.setOpenId("xxx");
		memberDao.persist(m);
		assertEquals(memberDao.findByOpenId("xxx"), m);

		TwigMemberDaoImpl dao2 = new TwigMemberDaoImpl();
		// init separate object data store for new dao
		dao2.ods = new AnnotationObjectDatastore();

		Member m2 = dao2.findByKey(m.getAutoId());
		assertNotSame(m2, m);
		m2.setDisplayName("Mr X");
		dao2.persist(m2);
		assertFalse("Mr X".equals(memberDao.findByKey(m.getAutoId()).getDisplayName()));
		((TwigMemberDaoImpl) memberDao).memberLastCacheByKeys.put(m.getAutoId(), System.currentTimeMillis()
				- TwigMemberDaoImpl.CACHE_PERIOD);
		assertEquals(memberDao.findByKey(m.getAutoId()).getDisplayName(), "Mr X");
	}

	@Test
	public void obsoleteMemberByOpenIdShouldBeRefreshed() {
		Member m = new Member();
		m.setOpenId("xxx");
		memberDao.persist(m);
		assertEquals(memberDao.findByOpenId("xxx"), m);

		TwigMemberDaoImpl dao2 = new TwigMemberDaoImpl();
		// init separate object data store for new dao
		dao2.ods = new AnnotationObjectDatastore();

		Member m2 = dao2.findByOpenId("xxx");
		assertNotSame(m2, m);
		m2.setDisplayName("Mr X");
		dao2.persist(m2);
		assertFalse("Mr X".equals(memberDao.findByOpenId("xxx").getDisplayName()));
		((TwigMemberDaoImpl) memberDao).memberLastCacheByOpenIds.put(m.getOpenId(), System.currentTimeMillis()
				- TwigMemberDaoImpl.CACHE_PERIOD);
		assertEquals(memberDao.findByOpenId("xxx").getDisplayName(), "Mr X");
	}
}