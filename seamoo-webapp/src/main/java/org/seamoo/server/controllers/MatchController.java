package org.seamoo.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/matches")
public class MatchController {
	@RequestMapping("/participate")
	public ModelAndView participate(@RequestParam("leagueId") long leagueId) {
		ModelAndView mav = new ModelAndView("matches.participate");
		mav.addObject("title", "Tham gia giải đấu");
		return mav;
	}

	@RequestMapping("/{matchId}/{matchDescription}")
	public ModelAndView view(@PathVariable("matchId") long matchId, @PathVariable("matchDescription") String matchDescription) {
		ModelAndView mav = new ModelAndView("matches.detail");
		mav.addObject("title", "Xem trận đấu");
		return mav;
	}

}
