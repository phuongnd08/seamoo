package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProfileController {
	@RequestMapping("/profiles/view")
	public ModelAndView view(@RequestParam("profileId") long profileId) {
		ModelAndView mav = new ModelAndView("profiles.view");
		return mav;
	}

	@RequestMapping("/profiles/edit")
	public ModelAndView edit(@RequestParam("profileId") long profileId) {
		ModelAndView mav = new ModelAndView("profiles.edit");
		return mav;
	}
}
