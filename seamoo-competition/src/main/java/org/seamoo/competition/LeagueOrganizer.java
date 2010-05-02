package org.seamoo.competition;

import java.util.HashMap;
import java.util.Map;

public class LeagueOrganizer {
	Map<Long, MatchOrganizer> organizers;

	public LeagueOrganizer() {
		organizers = new HashMap<Long, MatchOrganizer>();
	}

	public synchronized MatchOrganizer getMatchOrganizer(Long leagueId) {
		if (leagueId == null)
			throw new NullPointerException("leagueId cannot be null");
		if (!organizers.containsKey(leagueId)) {
			MatchOrganizer organizer = new MatchOrganizer(leagueId);
			organizers.put(leagueId, organizer);
			return organizer;
		}
		return organizers.get(leagueId);
	}
}
