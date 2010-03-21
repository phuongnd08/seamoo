package org.seamoo.webapp.controllers;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.testng.JMockitTestNG;

import org.seamoo.persistence.SiteSettingDAO;
import org.seamoo.webapp.Site;
import org.springframework.web.servlet.ModelAndView;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AdminControllerTest extends JMockitTestNG {
	@Mocked
	SiteSettingDAO siteSettingDAO;

	AdminController adminController;

	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		adminController = new AdminController();
		adminController.siteSettingDAO = siteSettingDAO;

	}

	@Test
	public void manageSiteSettingShouldReturnCurrentOperatingMode() {
		new NonStrictExpectations() {
			{
				siteSettingDAO.getSetting("operatingMode");
				result = "normal";
			}
		};
		siteSettingDAO.assignSetting("operatingMode", "normal");
		ModelAndView mav = adminController.manageSiteSettings();
		Map<String, Object> model = mav.getModel();
		assertEquals("normal", model.get("operatingMode"));
	}

	@Test
	public void manageSiteSettingShouldReturnBootstrapModeByDefault() {
		new NonStrictExpectations() {
			{
				siteSettingDAO.getSetting("operatingMode");
				result = null;
			}
		};
		ModelAndView mav = adminController.manageSiteSettings();
		Map<String, Object> model = mav.getModel();
		assertEquals(Site.OPERATING_MODE_BOOTSTRAP, model.get("operatingMode"));
	}
}
