package org.seamoo.daos;

import java.util.List;

import org.seamoo.entities.LeagueMembership;

public interface LeagueMembershipDao extends GenericDao<LeagueMembership, Long> {
	LeagueMembership findByMemberAndLeagueAndMoment(Long memberAutoId, Long leagueAutoId, int year, int month);
	LeagueMembership findByMemberAndLeagueAtCurrentMoment(Long memberAutoId, Long leagueAutoId);
	List<LeagueMembership> findUndeterminedByMinimumAutoIdAndMoment(int year, int month, int startFrom, int count);
	List<LeagueMembership> getByLeagueAndRanking(Long leagueAutoId, long from, int count);
	long countByLeague(Long leagueAutoId);
	long countByMember(Long memberAutoId);
	List<LeagueMembership> getRecentByMember(Long memberAutoId, long from, int count);
}
