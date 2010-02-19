package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LeagueController {
	@RequestMapping("/leagues/list")
	public ModelAndView list(@RequestParam("subjectId") long subjectId) {
		ModelAndView mav = new ModelAndView("leagues.list");
		return mav;
	}

	@RequestMapping("/leagues/view")
	public ModelAndView detail(@RequestParam("leagueId") long leagueId) {
		ModelAndView mav = new ModelAndView("leagues.detail");
		return mav;
	}
}
