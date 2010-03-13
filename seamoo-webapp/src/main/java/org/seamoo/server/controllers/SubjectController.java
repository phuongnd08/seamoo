package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
	@RequestMapping(value={"/", ""}) //all: subjects, subjects/, subjects/list will be routed to this method 
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("subjects.list");
		mav.addObject("title", "Subject lists");
		return mav;
	}

	@RequestMapping("/view/{subjectId}")
	public ModelAndView view(@PathVariable("subjectId") long subjectId) {
		ModelAndView mav = new ModelAndView("subjects.detail");
		mav.addObject("title", "English");
		return mav;
	}
}
