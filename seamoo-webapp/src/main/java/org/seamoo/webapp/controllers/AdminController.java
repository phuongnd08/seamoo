package org.seamoo.webapp.controllers;

import org.seamoo.persistence.daos.SiteSettingDao;
import org.seamoo.webapp.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	SiteSettingDao siteSettingDao;

	@RequestMapping("/moderators")
	public ModelAndView manageModerators() {
		ModelAndView mav = new ModelAndView("admin.moderators");
		mav.addObject("title", "Manage moderators");
		return mav;
	}

	@RequestMapping("/leagues")
	public ModelAndView manageLeagues() {
		ModelAndView mav = new ModelAndView("admin.leagues");
		mav.addObject("title", "Manage leagues");
		return mav;
	}

	@RequestMapping("/subjects")
	public ModelAndView manageSubjects() {
		ModelAndView mav = new ModelAndView("admin.subjects");
		mav.addObject("title", "Manage subjects");
		return mav;
	}

	@RequestMapping("/site-settings")
	public ModelAndView manageSiteSettings() {
		ModelAndView mav = new ModelAndView("admin.site-settings");
		mav.addObject("title", "Manage site settings");
		String operatingMode = siteSettingDao.getSetting("operatingMode");
		if (operatingMode == null)
			operatingMode = Site.OPERATING_MODE_DEFAULT;
		mav.addObject("operatingMode", operatingMode);
		return mav;
	}
	
	@RequestMapping(value="/update-site-settings", method=RequestMethod.POST)
	public String updateSiteSettings(@RequestParam("operatingMode") String operatingMode){
		siteSettingDao.assignSetting("operatingMode", operatingMode);
		return "redirect:site-settings";
	}
}
