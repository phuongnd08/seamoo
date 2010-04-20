package org.seamoo.daos.twigImpl;

import static org.testng.Assert.*;

import org.seamoo.daos.MemberDao;
import org.seamoo.entities.Member;
import org.seamoo.persistence.test.LocalDatastoreTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TwigMemberDaoImplTest extends LocalDatastoreTest {
	MemberDao memberDao;

	public TwigMemberDaoImplTest() {
	}

	@Override
	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		memberDao = new TwigMemberDaoImpl();
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
		assertEquals(null, memberDao.findByOpenId("non-existent-open-id"));
	}

}