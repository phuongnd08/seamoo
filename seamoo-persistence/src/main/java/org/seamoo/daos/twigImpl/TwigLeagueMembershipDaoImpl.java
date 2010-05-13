package org.seamoo.daos.twigImpl;

import org.seamoo.daos.LeagueMembershipDao;
import org.seamoo.entities.LeagueMembership;
import org.seamoo.utils.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigLeagueMembershipDaoImpl extends TwigGenericDaoImpl<LeagueMembership, Long> implements LeagueMembershipDao {

	@Autowired
	TimeProvider timeProvider;

	@Override
	public LeagueMembership findByMemberAndLeagueAtMoment(Long memberAutoId, Long leagueAutoId, int year, int month) {
		RootFindCommand<LeagueMembership> fc = getOds().find().type(LeagueMembership.class).addFilter("memberAutoId",
				FilterOperator.EQUAL, memberAutoId).addFilter("leagueAutoId", FilterOperator.EQUAL, leagueAutoId).addFilter(
				"year", FilterOperator.EQUAL, year).addFilter("month", FilterOperator.EQUAL, month);
		if (fc.countResultsNow() == 0)
			return null;
		return fc.returnResultsNow().next();
	}

	@Override
	public LeagueMembership findByMemberAndLeagueAtCurrentMoment(Long memberAutoId, Long leagueAutoId) {
		// TODO Auto-generated method stub
		return findByMemberAndLeagueAtMoment(memberAutoId, leagueAutoId, timeProvider.getCurrentYear(),
				timeProvider.getCurrentMonth());
	}
}
