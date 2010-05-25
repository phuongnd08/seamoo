package org.seamoo.webapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.seamoo.competition.LeagueOrganizer;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.LeagueMembershipDao;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.MemberQualificationDao;
import org.seamoo.daos.SubjectDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.entities.League;
import org.seamoo.entities.LeagueMembership;
import org.seamoo.entities.Member;
import org.seamoo.entities.MemberQualification;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.webapp.Pager;
import org.seamoo.webapp.filters.MemberInjectionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/leagues/")
public class LeagueController {

	@Autowired
	LeagueDao leagueDao;
	@Autowired
	SubjectDao subjectDao;
	@Autowired
	MatchDao matchDao;
	@Autowired
	LeagueMembershipDao leagueMembershipDao;
	@Autowired
	MemberQualificationDao memberQualificationDao;
	@Autowired
	LeagueOrganizer leagueOrganizer;
	@Autowired
	MemberDao memberDao;

	public int MATCH_PER_PAGE = 20;
	public int RANK_PER_PAGE = 10;

	@RequestMapping("/{leagueId}/{leagueAlias}")
	public ModelAndView detail(HttpServletRequest request, @PathVariable("leagueId") long leagueId,
			@RequestParam(value = "m", defaultValue = "1", required = false) long matchPage,
			@RequestParam(value = "r", defaultValue = "1") long rankPage) {
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
		mav.addObject("subject", subjectDao.findByKey(l.getSubjectAutoId()));
		long matchPageCount = Pager.getPageCount(matchDao.countByLeague(leagueId), MATCH_PER_PAGE);
		long rankPageCount = Pager.getPageCount(leagueMembershipDao.countByLeague(leagueId), RANK_PER_PAGE);
		List<LeagueMembership> ranks = leagueMembershipDao.getByLeagueAndRanking(leagueId, Pager.getFromForPage(rankPage,
				RANK_PER_PAGE), RANK_PER_PAGE);
		List<Match> matches = matchDao.getRecentMatchesByLeague(leagueId, Pager.getFromForPage(matchPage, MATCH_PER_PAGE),
				MATCH_PER_PAGE);
		mav.addObject("matchPage", matchPage);
		mav.addObject("rankPage", rankPage);
		mav.addObject("matches", matches);
		mav.addObject("ranks", ranks);
		mav.addObject("rankFrom", Pager.getFromForPage(rankPage, RANK_PER_PAGE) + 1);
		mav.addObject("matchPageCount", matchPageCount);
		mav.addObject("rankPageCount", rankPageCount);

		Map<Long, Boolean> requiredMember = new HashMap<Long, Boolean>();

		Map<String, Member> memberMap = new HashMap<String, Member>();
		for (Match match : matches) {
			for (MatchCompetitor competitor : match.getCompetitors())
				if (!memberMap.containsKey(competitor.getMemberAutoId()))
					memberMap.put(competitor.getMemberAutoId().toString(), memberDao.findByKey(competitor.getMemberAutoId()));
		}
		for (LeagueMembership rank : ranks) {
			if (!memberMap.containsKey(rank.getMemberAutoId().toString())) {
				memberMap.put(rank.getMemberAutoId().toString(), memberDao.findByKey(rank.getMemberAutoId()));
			}
		}
		mav.addObject("memberMap", memberMap);
		return mav;
	}
}
