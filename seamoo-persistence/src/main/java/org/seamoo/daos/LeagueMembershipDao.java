package org.seamoo.daos;

import java.util.List;

import org.seamoo.entities.LeagueMembership;

public interface LeagueMembershipDao extends GenericDao<LeagueMembership, Long> {
	LeagueMembership findByMemberAndLeagueAndMoment(Long memberAutoId, Long leagueAutoId, int year, int month);
	LeagueMembership findByMemberAndLeagueAtCurrentMoment(Long memberAutoId, Long leagueAutoId);
	List<LeagueMembership> findUndeterminedByMinimumAutoIdAndMoment(int year, int month, int startFrom, int count);
}
