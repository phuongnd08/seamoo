package org.seamoo.webapp.controllers;

import org.seamoo.daos.LeagueDao;
import org.seamoo.entities.League;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/leagues/")
public class LeagueController {

	@Autowired
	LeagueDao leagueDao;

	@RequestMapping("/{leagueId}/{leagueAlias}")
	public ModelAndView detail(@PathVariable("leagueId") long leagueId) {
		ModelAndView mav = new ModelAndView("leagues.detail");
		League l = leagueDao.findByKey(leagueId); 
		mav.addObject("title", l.getName());
		mav.addObject("nextLeague", leagueDao.findBySubjectIdAndLevel(l.getSubjectAutoId(), l.getLevel() + 1));
		mav.addObject("league", l);
		return mav;
	}
}
