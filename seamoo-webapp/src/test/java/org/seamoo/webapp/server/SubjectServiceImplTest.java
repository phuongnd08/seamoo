package org.seamoo.webapp.server;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.Arrays;

import org.seamoo.daos.SubjectDao;
import org.seamoo.entities.Subject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SubjectServiceImplTest {

	SubjectDao subjectDao;
	SubjectServiceImpl subjectServiceImpl;

	@BeforeMethod
	public void setUp() {
		subjectDao = mock(SubjectDao.class);
		when(subjectDao.getAll()).thenReturn(Arrays.asList(new Subject[] { new Subject(), new Subject() }));
		subjectServiceImpl = new SubjectServiceImpl();
		subjectServiceImpl.subjectDao = subjectDao;
	}

	@Test
	public void subjectServiceImplReturnSubjectListAccordingToSubjectDao() {
		assertEquals(subjectServiceImpl.getAll().size(), 2);

	}
}
