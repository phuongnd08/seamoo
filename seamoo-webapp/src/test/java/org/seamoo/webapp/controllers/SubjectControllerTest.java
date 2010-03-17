package org.seamoo.webapp.controllers;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seamoo.entities.Subject;
import org.seamoo.persistence.SubjectDAO;
import org.seamoo.persistence.test.LocalDatastoreTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SubjectControllerTest extends LocalDatastoreTest {

	@Autowired
	private SubjectDAO subjectDAO;
	
	Subject english, maths, literature;
	SubjectController subjectController ;
	@Override
	@Before
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		Subject[] subjects = new Subject[]{english=new Subject(), maths=new Subject(), literature=new Subject()};
		english.setName("English");
		maths.setName("Maths");
		literature.setName("Literature");
		english.setEnabled(true);
		maths.setEnabled(true);
		literature.setEnabled(false);
		subjectDAO.persist(subjects);
		subjectController = new SubjectController();
		subjectController.subjectDAO = subjectDAO;

	}

	@Override
	@After
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	
	@Test
	public void listSubjectReturnsEnabledSubject(){
		
		ModelAndView mav= subjectController.list();
		Map<String, Object> model = mav.getModel();
		List<Subject> returnedSubjects= (List<Subject>) model.get("subjects");
		assertEquals(2, returnedSubjects.size());
	}
	
	@Test
	public void viewOfEnabledSubjectFromCommonUserReturnSubject(){
		subjectDAO.persist(english);
		ModelAndView mav = subjectController.view(1);
		Map<String, Object> model= mav.getModel();
		Subject subject = (Subject) model.get("subject");
		assertEquals("English", subject.getName());
	}
	
	@Test
	@ExpectedException(SecurityException.class)
	public void viewOfDisabledSubjectFromCommonUserThrowExceptions(){
		
	}

}
