package org.seamoo.webapp.controllers;

import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.SubjectDao;
import org.seamoo.entities.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

	@Autowired
	SubjectDao subjectDao;
	@Autowired
	LeagueDao leagueDao;

	@RequestMapping(value = { "/", "" })
	// subjects, subjects/ will be routed to this method
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("subjects.list");
		mav.addObject("title", "Subject lists");
		mav.addObject("subjects", subjectDao.getEnabledSubjects());
		return mav;
	}

	@RequestMapping("/{subjectId}/{subjectAlias}")
	public ModelAndView view(@PathVariable("subjectId") long subjectId, @PathVariable("subjectAlias") String subjectAlias) {
		ModelAndView mav = new ModelAndView("subjects.detail");
		Subject s = subjectDao.findByKey(subjectId);
		mav.addObject("title", s.getName());
		mav.addObject("subject", s);
		mav.addObject("leagues", leagueDao.getEnabledBySubjectId(subjectId));
		return mav;
	}
}
