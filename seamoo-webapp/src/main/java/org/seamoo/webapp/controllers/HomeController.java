package org.seamoo.webapp.controllers;

import org.seamoo.daos.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	@Autowired
	SubjectDao subjectDao;

	@RequestMapping("/home")
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView("home");
		mav.addObject("subjects", subjectDao.getEnabledSubjects());
		mav.addObject("title", "Home");
		return mav;
	}

	@RequestMapping("/faq")
	public ModelAndView faq() {
		ModelAndView mav = new ModelAndView("faq");
		mav.addObject("title", "FAQ");
		return mav;
	}

	@RequestMapping("/about")
	public ModelAndView about() {
		ModelAndView mav = new ModelAndView("about");
		mav.addObject("title", "About");
		return mav;
	}
}
