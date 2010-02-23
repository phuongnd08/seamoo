package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/leagues/")
public class LeagueController {

	@RequestMapping("/view/{leagueId}")
	public ModelAndView detail(@PathVariable("leagueId") long leagueId) {
		ModelAndView mav = new ModelAndView("leagues.detail");
		mav.addObject("title", "Giải gà con");
		return mav;
	}
}
