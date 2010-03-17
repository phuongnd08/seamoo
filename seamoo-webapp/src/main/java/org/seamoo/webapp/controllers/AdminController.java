package org.seamoo.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/admin")
public class AdminController {

	@RequestMapping("/moderators")
	public ModelAndView manageModerators(){
		ModelAndView mav = new ModelAndView("admin.moderators");
		mav.addObject("title", "Manage moderators");
		return mav;
	}
	
	@RequestMapping("/leagues")
	public ModelAndView manageLeagues(){
		ModelAndView mav = new ModelAndView("admin.leagues");
		mav.addObject("title", "Manage leagues");
		return mav;
	}
	
	@RequestMapping("/subjects")
	public ModelAndView manageSubjects(){
		ModelAndView mav = new ModelAndView("admin.subjects");
		mav.addObject("title", "Manage subjects");
		return mav;
	}
	
	@RequestMapping("/site-settings")
	public ModelAndView manageSiteSettings(){
		ModelAndView mav = new ModelAndView("admin.site-settings");
		mav.addObject("title", "Manage site settings");
		return mav;
	}
}
