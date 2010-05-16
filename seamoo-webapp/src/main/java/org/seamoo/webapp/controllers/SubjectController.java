package org.seamoo.webapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.seamoo.competition.LeagueOrganizer;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.LeagueMembershipDao;
import org.seamoo.daos.MemberQualificationDao;
import org.seamoo.daos.SubjectDao;
import org.seamoo.entities.League;
import org.seamoo.entities.LeagueMembership;
import org.seamoo.entities.Member;
import org.seamoo.entities.MemberQualification;
import org.seamoo.entities.Subject;
import org.seamoo.webapp.filters.MemberInjectionFilter;
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
	@Autowired
	LeagueOrganizer leagueOrganizer;
	@Autowired
	LeagueMembershipDao leagueMembershipDao;
	@Autowired
	MemberQualificationDao memberQualificationDao;

	@RequestMapping(value = { "/", "" })
	// subjects, subjects/ will be routed to this method
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("subjects.list");
		mav.addObject("title", "Subject lists");
		mav.addObject("subjects", subjectDao.getEnabledSubjects());
		return mav;
	}

	@RequestMapping("/{subjectId}/{subjectAlias}")
	public ModelAndView view(HttpServletRequest request, @PathVariable("subjectId") long subjectId,
			@PathVariable("subjectAlias") String subjectAlias) {
		ModelAndView mav = new ModelAndView("subjects.detail");
		Subject s = subjectDao.findByKey(subjectId);
		mav.addObject("title", s.getName());
		mav.addObject("subject", s);
		List<League> leagues = leagueDao.getEnabledBySubjectId(subjectId);
		mav.addObject("leagues", leagues);
		Member member = MemberInjectionFilter.getInjectedMember(request);
		if (member != null) {
			MemberQualification mq = memberQualificationDao.findByMemberAndSubject(member.getAutoId(), subjectId);
			Map<String, Boolean> joinable = new HashMap<String, Boolean>();
			Map<Integer, League> leagueByLevel = new HashMap<Integer, League>();
			Map<String, League> nextLeagues = new HashMap<String, League>();
			Map<String, LeagueMembership> memberships = new HashMap<String, LeagueMembership>();

			for (League l : leagues) {
				boolean canJoin = leagueOrganizer.canMemberJoinLeague(mq, l);
				joinable.put(l.getAutoId().toString(), canJoin);
				if (canJoin) {
					memberships.put(l.getAutoId().toString(), leagueMembershipDao.findByMemberAndLeagueAtCurrentMoment(
							member.getAutoId(), l.getAutoId()));
				}
				leagueByLevel.put(l.getLevel(), l);
			}

			for (League l : leagues) {
				nextLeagues.put(l.getAutoId().toString(), leagueByLevel.get(l.getLevel() + 1));
			}

			mav.addObject("joinable", joinable);
			mav.addObject("nextLeagues", nextLeagues);
			mav.addObject("memberships", memberships);
		}

		return mav;
	}
}
