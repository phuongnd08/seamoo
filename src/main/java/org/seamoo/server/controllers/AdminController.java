package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class AdminController {

	@RequestMapping("/admin/assign-moderators")
	public ModelAndView assignModerators(){
		ModelAndView mav = new ModelAndView("admin.assign-moderators");
		return mav;
	}
	
	@RequestMapping("/admin/manage-leagues")
	public ModelAndView manageLeagues(){
		ModelAndView mav = new ModelAndView("admin.manage-leagues");
		return mav;
	}
	
	@RequestMapping("/admin/manage-subjects")
	public ModelAndView manageSubjects(){
		ModelAndView mav = new ModelAndView("admin.manage-subjects");
		return mav;
	}
	
	@RequestMapping("/admin/manage-site-settings")
	public ModelAndView manageSiteSettings(){
		ModelAndView mav = new ModelAndView("admin.manage-site-settings");
		return mav;
	}
}
