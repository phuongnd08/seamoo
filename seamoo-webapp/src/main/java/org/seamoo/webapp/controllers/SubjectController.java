package org.seamoo.webapp.controllers;

import org.seamoo.persistence.SubjectDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/subjects")
public class SubjectController {
	
	@Autowired
	SubjectDAO subjectDAO;
	
	@RequestMapping(value={"/", ""}) //all: subjects, subjects/, subjects/list will be routed to this method 
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("subjects.list");
		mav.addObject("title", "Subject lists");
		mav.addObject("subjects", subjectDAO.getEnabledSubjects());
		return mav;
	}

	@RequestMapping("/view/{subjectId}")
	public ModelAndView view(@PathVariable("subjectId") long subjectId) {
		ModelAndView mav = new ModelAndView("subjects.detail");
		mav.addObject("title", "English");
		mav.addObject("subject", subjectDAO.findById(subjectId));
		return mav;
	}
}
