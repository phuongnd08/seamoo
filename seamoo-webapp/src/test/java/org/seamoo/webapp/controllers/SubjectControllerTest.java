package org.seamoo.webapp.controllers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.seamoo.entities.Subject;
import org.seamoo.persistence.daos.SubjectDao;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SubjectControllerTest {

	SubjectDao subjectDAO;

	Subject english, maths, literature;

	SubjectController subjectController;

	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		english = new Subject();
		maths = new Subject();
		literature = new Subject();

		english.setName("English");
		maths.setName("Maths");
		literature.setName("Literature");
		english.setEnabled(true);
		maths.setEnabled(true);
		literature.setEnabled(false);

		subjectController = new SubjectController();
		subjectDAO = mock(SubjectDao.class);
		subjectController.subjectDao = subjectDAO;

	}

	@Test
	public void listSubjectReturnsEnabledSubject() {
		when(subjectDAO.getEnabledSubjects()).thenReturn(Arrays.asList(new Subject[] { english, maths }));
		ModelAndView mav = subjectController.list();
		Map<String, Object> model = mav.getModel();
		List<Subject> returnedSubjects = (List<Subject>) model.get("subjects");
		assertEquals(2, returnedSubjects.size());
	}

	@Test
	public void viewOfEnabledSubjectShouldReturnSubject() {
		when(subjectDAO.findById(1L)).thenReturn(english);
		ModelAndView mav = subjectController.view(1, "english");
		Map<String, Object> model = mav.getModel();
		Subject subject = (Subject) model.get("subject");
		assertEquals("English", subject.getName());
	}

}
