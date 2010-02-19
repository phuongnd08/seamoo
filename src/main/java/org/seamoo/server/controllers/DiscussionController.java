package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DiscussionController {
	@RequestMapping("/discussion/personal")
	public ModelAndView viewPersonal() {
		ModelAndView mav = new ModelAndView("discussion.personal");
		return mav;
	}

	@RequestMapping("/discussion/all")
	public ModelAndView viewAll() {
		ModelAndView mav = new ModelAndView("discussion.all");
		return mav;
	}

}
