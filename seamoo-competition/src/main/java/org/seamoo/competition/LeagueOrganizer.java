package org.seamoo.competition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seamoo.cache.RemoteObjectFactory;
import org.seamoo.competition.MatchOrganizer.EventListener;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.LeagueMembershipDao;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.MemberQualificationDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.League;
import org.seamoo.entities.LeagueMembership;
import org.seamoo.entities.LeagueResult;
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
	RemoteObjectFactory cacheWrapperFactory;

	TimeProvider timeProvider = TimeProvider.DEFAULT;

	LeagueOrganizerSettings settings;

	Map<Long, MatchOrganizer> organizers;

	EventListener listener = new EventListener() {

		@Override
		public void finishMatch(Match match) {
			updateLeagueMembershipScore(match);
		}
	};

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
			organizer.addEventListener(listener);
			organizers.put(leagueId, organizer);
			return organizer;
		}
		return organizers.get(leagueId);
	}

	public boolean canMemberJoinLeague(MemberQualification mq, League league) {
		if (league.getLevel() == 0 && mq == null)
			return true;
		if (mq == null)
			return false;
		return mq.getLevel() == league.getLevel() || mq.getLevel() - 1 == league.getLevel();
	}

	public void updateLeagueMembershipScore(Match rankedMatch) {
		for (MatchCompetitor competitor : rankedMatch.getCompetitors()) {
			LeagueMembership lms = leagueMembershipDao.findByMemberAndLeagueAtCurrentMoment(competitor.getMemberAutoId(),
					rankedMatch.getLeagueAutoId());
			if (lms == null) {
				lms = new LeagueMembership();
				lms.setMemberAutoId(competitor.getMemberAutoId());
				lms.setLeagueAutoId(rankedMatch.getLeagueAutoId());
				lms.setYear(timeProvider.getCurrentYear());
				lms.setMonth(timeProvider.getCurrentMonth());
			}
			double additionScore = 0;
			if (competitor.getTotalScore() > settings.getMinMatchScoreForAccumulation()) {
				additionScore = settings.getMaxScorePerMatch() * settings.getRankRatio(competitor.getRank());
			}
			competitor.setAdditionalAccumulatedScore(additionScore);
			lms.setAccumulatedScore(lms.getAccumulatedScore() + additionScore);
			lms.setMatchCount(lms.getMatchCount() + 1);
			leagueMembershipDao.persist(lms);
		}
	}

	/**
	 * Method to be used in *cron* job to periodically relegate member between different league. The method will take a
	 * LeagueMembership whose id>fromLeagueMembershipAutoId and check if the user should be relegate (by updating his level).
	 * After running for an amount of maxTime, the method will return a membershipAutoId that can be used to queue another *cron*
	 * job (this is due to the limitation on execution time of a request). In the case all record are scanned, the method return
	 * -1, indicate no more check is necessary (for current month)
	 * 
	 * @param fromLeagueIndex
	 * @param maxTime
	 * @return
	 */
	public long recheckLeagueMembership(int startFrom, long maxTime) {
		long currentTime = timeProvider.getCurrentTimeStamp();
		int yearToProcess = timeProvider.getCurrentYear();
		int updatedYear = timeProvider.getCurrentYear();
		int monthToProcess = timeProvider.getCurrentMonth() - 1;
		int updatedMonth = timeProvider.getCurrentMonth();
		if (monthToProcess == 0) {
			monthToProcess = 12;
			yearToProcess--;
		}
		List<LeagueMembership> memberships = null;
		boolean done = false;
		int rangeStart = startFrom;
		while (timeProvider.getCurrentTimeStamp() - currentTime < maxTime && !done) {
			memberships = leagueMembershipDao.findUndeterminedByMinimumAutoIdAndMoment(yearToProcess, monthToProcess, rangeStart,
					settings.getCheckBatchSize());
			for (LeagueMembership ms : memberships) {
				League l = leagueDao.findByKey(ms.getLeagueAutoId());
				MemberQualification mq = memberQualificationDao.findByMemberAndSubject(ms.getMemberAutoId(), l.getSubjectAutoId());
				if (mq == null) {
					mq = new MemberQualification();
					mq.setMemberAutoId(ms.getMemberAutoId());
					mq.setSubjectAutoId(l.getSubjectAutoId());
				}
				int levelToRelegate;
				if (ms.getAccumulatedScore() >= settings.getScoreForUpRelegate()) {
					levelToRelegate = l.getLevel() + 1;
					ms.setResult(LeagueResult.UP_RELEGATED);
				} else if (ms.getAccumulatedScore() >= settings.getScoreForStay()) {
					levelToRelegate = l.getLevel();
					ms.setResult(LeagueResult.STAYED);
				} else {
					levelToRelegate = l.getLevel() != 0 ? l.getLevel() - 1 : 0;
					ms.setResult(LeagueResult.DOWN_RELEGATED);
				}
				if (mq.getUpdatedYear() != updatedYear || mq.getUpdatedMonth() != updatedMonth) {
					mq.setUpdatedMonth(updatedMonth);
					mq.setUpdatedYear(updatedYear);
					mq.setLevel(levelToRelegate);
				} else {
					mq.setLevel(Math.max(mq.getLevel(), levelToRelegate));
					// in case user participate in multiple leagues, take
					// account the best
				}
				ms.setResultCalculated(true);
				memberQualificationDao.persist(mq);
				leagueMembershipDao.persist(ms);
			}
			done = memberships.size() < settings.getCheckBatchSize();
			rangeStart = rangeStart + settings.getCheckBatchSize();
		}

		if (done)
			return -1;
		return rangeStart;
	}

}
