package org.seamoo.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/training")
public class TrainingController {
	@RequestMapping("/go")
	public ModelAndView go(){
		ModelAndView mav = new ModelAndView("training.go");
		mav.addObject("title", "Luyện tập");
		return mav;
	}
	
	@RequestMapping("/personal-collection")
	public ModelAndView viewPersonalCollection(){
		ModelAndView mav = new ModelAndView("training.personal-collection");
		mav.addObject("title", "Bộ sưu tập cá nhân");
		return mav;
	}
}
