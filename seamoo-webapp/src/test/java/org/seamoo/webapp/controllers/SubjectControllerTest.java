package org.seamoo.webapp.controllers;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.testng.JMockitTestNG;

import org.seamoo.entities.Subject;
import org.seamoo.persistence.SubjectDAO;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SubjectControllerTest extends JMockitTestNG {

	@Mocked
	SubjectDAO subjectDAO;

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
		subjectController.subjectDAO = subjectDAO;

	}

	@Test
	public void listSubjectReturnsEnabledSubject() {
		new NonStrictExpectations() {
			{
				subjectDAO.getEnabledSubjects();
				result = Arrays.asList(new Subject[] { english, maths });
			}
		};
		ModelAndView mav = subjectController.list();
		Map<String, Object> model = mav.getModel();
		List<Subject> returnedSubjects = (List<Subject>) model.get("subjects");
		assertEquals(2, returnedSubjects.size());
	}

	@Test
	public void viewOfEnabledSubjectShouldReturnSubject() {
		new NonStrictExpectations() {
			{
				subjectDAO.findById(1L);
				result = english;
			}
		};
		ModelAndView mav = subjectController.view(1);
		Map<String, Object> model = mav.getModel();
		Subject subject = (Subject) model.get("subject");
		assertEquals("English", subject.getName());
	}

	@Test(expectedExceptions = SecurityException.class)
	public void viewOfDisabledSubjectShouldNotReturnSubject() {
	}

}
