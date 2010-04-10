package org.seamoo.webapp.controllers;

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.seamoo.entities.Subject;
import org.seamoo.persistence.daos.SiteSettingDao;
import org.seamoo.persistence.daos.SubjectDao;
import org.seamoo.webapp.Site;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdminControllerTest {
	SiteSettingDao siteSettingDAO;
	SubjectDao subjectDao;

	AdminController adminController;

	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		siteSettingDAO = mock(SiteSettingDao.class);
		subjectDao = mock(SubjectDao.class);
		adminController = new AdminController();
		adminController.siteSettingDao = siteSettingDAO;
		adminController.subjectDao = subjectDao;

	}

	@Test
	public void manageSiteSettingShouldReturnCurrentOperatingMode() {
		when(siteSettingDAO.getSetting("operatingMode")).thenReturn("normal");
		siteSettingDAO.assignSetting("operatingMode", "normal");
		ModelAndView mav = adminController.manageSiteSettings();
		Map<String, Object> model = mav.getModel();
		assertEquals("normal", model.get("operatingMode"));
	}

	@Test
	public void manageSiteSettingShouldReturnBootstrapModeByDefault() {
		when(siteSettingDAO.getSetting("operatingMode")).thenReturn(null);
		ModelAndView mav = adminController.manageSiteSettings();
		Map<String, Object> model = mav.getModel();
		assertEquals(Site.OPERATING_MODE_BOOTSTRAP, model.get("operatingMode"));
	}

	@Test
	public void manageSubjectsShouldPassCurrentSubjectToView() {
		Subject currentSubject = new Subject();
		when(subjectDao.findById(1L)).thenReturn(currentSubject);
		ModelAndView mav = adminController.manageLeagues(1L);
		assertEquals(currentSubject, mav.getModel().get("currentSubject"));
	}
}
