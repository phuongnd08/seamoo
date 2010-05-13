package org.seamoo.daos;

import org.seamoo.entities.LeagueMembership;

public interface LeagueMembershipDao extends GenericDao<LeagueMembership, Long> {
	LeagueMembership findByMemberAndLeagueAtMoment(Long memberAutoId, Long leagueAutoId, int year, int month);
	LeagueMembership findByMemberAndLeagueAtCurrentMoment(Long memberAutoId, Long leagueAutoId);
}
