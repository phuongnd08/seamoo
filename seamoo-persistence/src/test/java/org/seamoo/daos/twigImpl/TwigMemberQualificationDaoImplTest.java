package org.seamoo.daos.twigImpl;

import static org.testng.Assert.*;

import org.seamoo.entities.MemberQualification;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TwigMemberQualificationDaoImplTest extends LocalAppEngineTest {

	@Override
	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
	}

	@Override
	@AfterMethod
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@Test
	public void getByMemberShouldReturnMatchingQualification() {
		MemberQualification mq = new MemberQualification();
		mq.setMemberAutoId(1L);
		mq.setLevel(2);
		mq.setSubjectAutoId(200L);
		TwigMemberQualificationDaoImpl daoImpl = new TwigMemberQualificationDaoImpl();
		daoImpl.persist(mq);
		assertNotNull(daoImpl.findByMemberAndSubject(1L, 200L));
		assertNull(daoImpl.findByMemberAndSubject(1L, 201L));
	}

}
