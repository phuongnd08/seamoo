package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	@RequestMapping("/create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView("questions.create");
		mav.addObject("title", "Tạo câu hỏi mới");
		return mav;
	}

	@RequestMapping("/edit/{questionId}")
	public ModelAndView edit(@PathVariable("questionId") long questionId) {
		ModelAndView mav = new ModelAndView("questions.edit");
		mav.addObject("title", "Sửa câu hỏi");
		return mav;
	}

	@RequestMapping("/{questionId}/{questionDescription}")
	public ModelAndView view(@PathVariable("questionId") long questionId, @PathVariable("questionDescription") String questionDescription) {
		ModelAndView mav = new ModelAndView("questions.detail");
		mav.addObject("title", "Xem câu hỏi");
		return mav;
	}
	
	@RequestMapping(value="/{questionId}/rev-{revNumber}/{revDescription}")
	public ModelAndView viewRevision(@PathVariable("questionId") long questionId, @PathVariable("revNumber") int revNumber, @PathVariable("revDescription") String revDescription){
		ModelAndView mav = new ModelAndView("questions.detail");
		mav.addObject("revisionToShow", revNumber);
		mav.addObject("title", String.format("Xem phiên bản %d của câu hỏi", revNumber));
		return mav;
	}
	
	@RequestMapping("/hot")
	public ModelAndView listHot(){
		ModelAndView mav = new ModelAndView("questions.hot");
		mav.addObject("title", "Câu hỏi nóng");
		return mav;
	}

}
