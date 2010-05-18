package org.seamoo.daos.matching;

import java.util.List;

import org.seamoo.daos.GenericDao;
import org.seamoo.entities.matching.Match;

public interface MatchDao extends GenericDao<Match, Long> {

	List<Match> getRecentMatchesByLeague(Long leagueAutoId, long from, int count);

	long countByLeague(Long leagueAutoId);
	
	List<Match> getRecentMatchesBymember(Long memberAutoId, long from, int count);
	
	long countByMember(Long memberAutoId);

}
