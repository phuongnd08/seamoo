package org.seamoo.daos.twigImpl;

import java.util.List;
import java.util.Date;
import org.seamoo.daos.SubjectDao;
import org.seamoo.entities.Subject;
import org.seamoo.persistence.test.LocalAppEngineTest;
import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TwigSubjectDaoImplTest extends LocalAppEngineTest {
	SubjectDao subjectDAO;
	Subject english, maths, literature;

	public TwigSubjectDaoImplTest() {
	}

	@Override
	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		subjectDAO = new TwigSubjectDaoImpl();
		Subject[] subjects = new Subject[] { english = new Subject(), maths = new Subject(), literature = new Subject() };
		english.setName("English");
		english.setAddedTime(new Date(2009, 12, 10));
		maths.setName("Maths");
		maths.setAddedTime(new Date(2008, 10, 31));
		literature.setName("Literature");
		literature.setAddedTime(new Date(2009, 6, 10));
		english.setEnabled(true);
		maths.setEnabled(true);
		subjectDAO.persist(subjects);
	}

	@Override
	@AfterMethod
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@Test
	public void getEnabledSubjectsShouldReturnedSubjectsInChronologicalOrder() {
		List<Subject> subjects = subjectDAO.getEnabledSubjects();
		assertEquals(2, subjects.size());
		assertEquals("Maths", subjects.get(0).getName());
		assertEquals("English", subjects.get(1).getName());
	}

	@Test
	public void getAllSubjectsShouldReturnedSubjectsInChronologicalOrder() {
		List<Subject> subjects = subjectDAO.getAll();
		assertEquals(3, subjects.size());
		assertEquals("Maths", subjects.get(0).getName());
		assertEquals("Literature", subjects.get(1).getName());
		assertEquals("English", subjects.get(2).getName());
	}
	
}
