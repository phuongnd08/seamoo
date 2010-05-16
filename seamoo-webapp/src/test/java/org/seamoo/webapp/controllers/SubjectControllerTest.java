package org.seamoo.webapp.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.SubjectDao;
import org.seamoo.entities.League;
import org.seamoo.entities.Subject;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SubjectControllerTest {

	SubjectDao subjectDao;
	LeagueDao leagueDao;

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
		subjectDao = mock(SubjectDao.class);
		leagueDao = mock(LeagueDao.class);
		subjectController.subjectDao = subjectDao;
		subjectController.leagueDao = leagueDao;

	}

	@Test
	public void listSubjectReturnsEnabledSubject() {
		when(subjectDao.getEnabledSubjects()).thenReturn(Arrays.asList(new Subject[] { english, maths }));
		ModelAndView mav = subjectController.list();
		Map<String, Object> model = mav.getModel();
		List<Subject> returnedSubjects = (List<Subject>) model.get("subjects");
		assertEquals(2, returnedSubjects.size());
	}

	@Test
	public void viewOfEnabledSubjectShouldReturnSubjectAndSubLeague() {
		when(subjectDao.findByKey(1L)).thenReturn(english);
		List<League> l = new ArrayList<League>();
		when(leagueDao.getEnabledBySubjectId(1L)).thenReturn(l);
		HttpServletRequest request = mock(HttpServletRequest.class);
		ModelAndView mav = subjectController.view(request, 1, "english");
		Map<String, Object> model = mav.getModel();
		Subject subject = (Subject) model.get("subject");
		assertEquals(subject, english);
		assertEquals(model.get("leagues"), l);
	}

}
