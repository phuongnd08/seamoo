package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/admin")
public class AdminController {

	@RequestMapping("/moderators")
	public ModelAndView manageModerators(){
		ModelAndView mav = new ModelAndView("admin.moderators");
		return mav;
	}
	
	@RequestMapping("/leagues")
	public ModelAndView manageLeagues(){
		ModelAndView mav = new ModelAndView("admin.leagues");
		return mav;
	}
	
	@RequestMapping("/subjects")
	public ModelAndView manageSubjects(){
		ModelAndView mav = new ModelAndView("admin.subjects");
		return mav;
	}
	
	@RequestMapping("/admin/site-settings")
	public ModelAndView manageSiteSettings(){
		ModelAndView mav = new ModelAndView("admin.site-settings");
		return mav;
	}
}
