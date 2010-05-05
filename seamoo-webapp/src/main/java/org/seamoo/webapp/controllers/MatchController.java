package org.seamoo.webapp.controllers;

import javax.servlet.http.HttpServletRequest;

import org.seamoo.competition.LeagueOrganizer;
import org.seamoo.competition.MatchOrganizer;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.entities.Member;
import org.seamoo.webapp.filters.MemberInjectionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/matches")
public class MatchController {
	@Autowired
	MatchDao matchDao;
	@Autowired
	LeagueDao leagueDao;
	@Autowired
	LeagueOrganizer leagueOrganizer;

	@RequestMapping("/participate")
	public ModelAndView participate(@RequestParam("leagueId") long leagueId) {
		ModelAndView mav = new ModelAndView("matches.participate");
		mav.addObject("title", "Tham gia giải đấu");
		mav.addObject("league", leagueDao.findByKey(leagueId));
		return mav;
	}

	@RequestMapping("/{matchId}/{matchDescription}")
	public ModelAndView view(@PathVariable("matchId") long matchId, @PathVariable("matchDescription") String matchDescription) {
		ModelAndView mav = new ModelAndView("matches.detail");
		mav.addObject("title", "Xem trận đấu");
		mav.addObject("match", matchDao.findByKey(matchId));
		return mav;
	}

	@RequestMapping("/rejoin")
	public String rejoin(HttpServletRequest request, @RequestParam("leagueId") long leagueId) {
		Member member = MemberInjectionFilter.getInjectedMember(request);
		leagueOrganizer.getMatchOrganizer(leagueId).escapeCurrentMatch(member.getAutoId());
		return "redirect:/matches/participate?leagueId=" + leagueId;
	}

}
