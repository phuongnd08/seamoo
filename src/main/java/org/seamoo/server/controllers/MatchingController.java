package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MatchingController {
	@RequestMapping("/matching/view")
	public ModelAndView view(@RequestParam("matchId") int matchId) {
		ModelAndView mav = new ModelAndView("matching.detail");
		return mav;
	}

	@RequestMapping("/matching/participate")
	public ModelAndView participate(@RequestParam("leagueId") long leagueId) {
		ModelAndView mav = new ModelAndView("matching.participate");
		return mav;
	}
}
