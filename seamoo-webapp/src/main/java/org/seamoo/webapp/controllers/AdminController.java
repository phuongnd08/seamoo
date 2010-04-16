package org.seamoo.webapp.controllers;

import org.seamoo.persistence.daos.SiteSettingDao;
import org.seamoo.persistence.daos.SubjectDao;
import org.seamoo.webapp.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	SiteSettingDao siteSettingDao;
	@Autowired
	SubjectDao subjectDao;

	@RequestMapping("/moderators")
	public ModelAndView manageModerators() {
		ModelAndView mav = new ModelAndView("admin.moderators");
		mav.addObject("title", "Manage moderators");
		return mav;
	}

	@RequestMapping("/subjects/{subjectAutoId}")
	public ModelAndView manageLeagues(@PathVariable long subjectAutoId) {
		ModelAndView mav = new ModelAndView("admin.leagues");
		mav.addObject("title", "Manage leagues");
		mav.addObject("currentSubject", subjectDao.findByKey(subjectAutoId));
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

	@RequestMapping(value = "/update-site-settings", method = RequestMethod.POST)
	public String updateSiteSettings(@RequestParam("operatingMode") String operatingMode) {
		siteSettingDao.assignSetting("operatingMode", operatingMode);
		return "redirect:site-settings";
	}
}
