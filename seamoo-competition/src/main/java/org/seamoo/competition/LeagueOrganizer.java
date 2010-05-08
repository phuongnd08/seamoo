package org.seamoo.competition;

import java.util.HashMap;
import java.util.Map;

import org.seamoo.cache.CacheWrapperFactory;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.question.QuestionDao;
import org.springframework.beans.factory.annotation.Autowired;

public class LeagueOrganizer {
	@Autowired
	MemberDao memberDao;
	@Autowired
	MatchDao matchDao;
	@Autowired
	QuestionDao questionDao;
	@Autowired
	CacheWrapperFactory cacheWrapperFactory;

	Map<Long, MatchOrganizer> organizers;

	public LeagueOrganizer() {
		organizers = new HashMap<Long, MatchOrganizer>();
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
}
