package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class QuestionController {
	@RequestMapping("/questions/create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView("questions.create");
		return mav;
	}

	@RequestMapping("/questions/edit")
	public ModelAndView edit(@RequestParam("questionId") long questionId) {
		ModelAndView mav = new ModelAndView("questions.edit");
		return mav;
	}

	@RequestMapping("/questions/view")
	public ModelAndView view(@RequestParam("questionId") long questionId) {
		ModelAndView mav = new ModelAndView("questions.detail");
		return mav;
	}
	
	@RequestMapping("/questions/view-revision")
	public ModelAndView viewRevision(@RequestParam("questionId") long questionId, @RequestParam("revNumber") int revNumber){
		ModelAndView mav = new ModelAndView("questions.view-revision");
		return mav;
	}
	
	@RequestMapping("/questions/list-hot")
	public ModelAndView listHot(){
		ModelAndView mav = new ModelAndView("questions.list-hot");
		return mav;
	}

}
