package org.seamoo.competition;

import java.util.HashMap;
import java.util.Map;

import org.seamoo.cache.CacheWrapperFactory;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.LeagueMembershipDao;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.MemberQualificationDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.League;
import org.seamoo.entities.LeagueMembership;
import org.seamoo.entities.MemberQualification;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.utils.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class LeagueOrganizer {
	@Autowired
	MemberDao memberDao;
	@Autowired
	MatchDao matchDao;
	@Autowired
	QuestionDao questionDao;
	@Autowired
	LeagueDao leagueDao;
	@Autowired
	MemberQualificationDao memberQualificationDao;
	@Autowired
	LeagueMembershipDao leagueMembershipDao;
	@Autowired
	CacheWrapperFactory cacheWrapperFactory;
	@Autowired
	TimeProvider timeProvider;

	LeagueOrganizerSettings settings;

	Map<Long, MatchOrganizer> organizers;

	public LeagueOrganizer() {
		this(new LeagueOrganizerSettings());
	}

	public LeagueOrganizer(LeagueOrganizerSettings settings) {
		organizers = new HashMap<Long, MatchOrganizer>();
		this.settings = settings;
	}

	public synchronized MatchOrganizer getMatchOrganizer(Long leagueId) {
		if (leagueId == null)
			throw new NullPointerException("leagueId cannot be null");
		if (!organizers.containsKey(leagueId)) {
			MatchOrganizer organizer = new MatchOrganizer(leagueId);
			organizer.memberDao = memberDao;
			organizer.matchDao = matchDao;
			organizer.questionDao = questionDao;
			organizer.cacheWrapperFactory = cacheWrapperFactory;
			organizers.put(leagueId, organizer);
			return organizer;
		}
		return organizers.get(leagueId);
	}

	public boolean canMemberJoinLeague(Long memberAutoId, Long leagueAutoId) {
		League league = leagueDao.findByKey(leagueAutoId);
		MemberQualification mq = memberQualificationDao.findByMember(memberAutoId);
		if (league.getLevel() == 0 && mq == null)
			return true;
		if (mq == null)
			return false;
		return mq.getLevel() == league.getLevel() || mq.getLevel() - 1 == league.getLevel();
	}

	public void updateLeagueMembershipScore(Match rankedMatch) {
		for (MatchCompetitor competitor : rankedMatch.getCompetitors()) {
			if (competitor.getTotalScore() > settings.getMinMatchScoreForAccumulation()) {
				LeagueMembership lms = leagueMembershipDao.findByMemberAndLeagueAtCurrentMoment(
						competitor.getMember().getAutoId(), rankedMatch.getLeagueAutoId());
				if (lms == null) {
					lms = new LeagueMembership();
					lms.setMemberAutoId(competitor.getMember().getAutoId());
					lms.setLeagueAutoId(rankedMatch.getLeagueAutoId());
					lms.setYear(timeProvider.getCurrentYear());
					lms.setMonth(timeProvider.getCurrentMonth());
				}
				double additionScore = settings.getMaxScorePerMatch() * settings.getRankRatio(competitor.getRank());
				lms.setAccumulatedScore(lms.getAccumulatedScore() + additionScore);
				leagueMembershipDao.persist(lms);
			}
		}
	}

	/**
	 * Method to be used in *cron* job to periodically relegate member between
	 * different league. The method will take a LeagueMembership whose
	 * id>fromLeagueMembershipAutoId and check if the user should be relegate
	 * (by updating his level). After running for an amount of maxTime, the
	 * method will return a membershipAutoId that can be used to queue another
	 * *cron* job (this is due to the limitation on execution time of a
	 * request). In the case all record are scanned, the method return -1,
	 * indicate no more check is necessary (for current month)
	 * 
	 * @param fromLeagueIndex
	 * @param maxTime
	 * @return
	 */
	public int recheckLeagueMembership(int fromLeagueMembershipAutoId, long maxTime) {
		return 0;
	}
}
