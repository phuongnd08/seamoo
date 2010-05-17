package org.seamoo.webapp.controllers;

import javax.servlet.http.HttpServletRequest;

import org.seamoo.competition.LeagueOrganizer;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.LeagueMembershipDao;
import org.seamoo.daos.MemberQualificationDao;
import org.seamoo.entities.League;
import org.seamoo.entities.Member;
import org.seamoo.entities.MemberQualification;
import org.seamoo.webapp.filters.MemberInjectionFilter;
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
	@Autowired
	LeagueMembershipDao leagueMembershipDao;
	@Autowired
	MemberQualificationDao memberQualificationDao;
	@Autowired
	LeagueOrganizer leagueOrganizer;

	@RequestMapping("/{leagueId}/{leagueAlias}")
	public ModelAndView detail(HttpServletRequest request, @PathVariable("leagueId") long leagueId) {
		ModelAndView mav = new ModelAndView("leagues.detail");
		League l = leagueDao.findByKey(leagueId);
		mav.addObject("title", l.getName());
		mav.addObject("nextLeague", leagueDao.findBySubjectIdAndLevel(l.getSubjectAutoId(), l.getLevel() + 1));
		Member member = MemberInjectionFilter.getInjectedMember(request);
		if (member != null) {
			MemberQualification mq = memberQualificationDao.findByMemberAndSubject(member.getAutoId(), l.getSubjectAutoId());
			boolean joinable = leagueOrganizer.canMemberJoinLeague(mq, l);
			mav.addObject("joinable", joinable);
			if (joinable)
				mav.addObject("membership",
						leagueMembershipDao.findByMemberAndLeagueAtCurrentMoment(member.getAutoId(), leagueId));
		}
		mav.addObject("league", l);
		return mav;
	}
}
