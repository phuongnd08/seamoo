package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/discussion")
public class DiscussionController {
	@RequestMapping(value = { "/", "" })
	public ModelAndView viewAll() {
		ModelAndView mav = new ModelAndView("discussion.all");
		mav.addObject("title", "Danh sách các trao đổi");
		return mav;
	}

}
