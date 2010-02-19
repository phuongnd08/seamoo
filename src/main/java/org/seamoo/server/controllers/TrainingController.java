package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TrainingController {
	@RequestMapping("/training/go")
	public ModelAndView go(){
		ModelAndView mav = new ModelAndView("training.go");
		return mav;
	}
	
	@RequestMapping("/training/view-personal-collection")
	public ModelAndView viewPersonalCollection(){
		ModelAndView mav = new ModelAndView("training.view-personal-collection");
		return mav;
	}
}
