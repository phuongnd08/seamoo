package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SubjectController {
	@RequestMapping("/subjects/list")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("subjects.list");
		mav.addObject("title", "Subject lists");
		return mav;
	}

	@RequestMapping("/subjects/view")
	public ModelAndView view(@RequestParam("subjectId") long subjectId) {
		ModelAndView mav = new ModelAndView("subjects.detail");
		return mav;
	}
}
