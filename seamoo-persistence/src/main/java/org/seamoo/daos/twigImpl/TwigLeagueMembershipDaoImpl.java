package org.seamoo.daos.twigImpl;

import java.util.List;

import org.seamoo.daos.LeagueMembershipDao;
import org.seamoo.entities.LeagueMembership;
import org.seamoo.entities.LeagueResult;
import org.seamoo.utils.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigLeagueMembershipDaoImpl extends TwigGenericDaoImpl<LeagueMembership, Long> implements LeagueMembershipDao {

	TimeProvider timeProvider = TimeProvider.DEFAULT;

	@Override
	public LeagueMembership findByMemberAndLeagueAndMoment(Long memberAutoId, Long leagueAutoId, int year, int month) {
		RootFindCommand<LeagueMembership> fc = getOds().find().type(LeagueMembership.class).addFilter("memberAutoId",
				FilterOperator.EQUAL, memberAutoId).addFilter("leagueAutoId", FilterOperator.EQUAL, leagueAutoId).addFilter(
				"year", FilterOperator.EQUAL, year).addFilter("month", FilterOperator.EQUAL, month);
		if (fc.countResultsNow() == 0)
			return null;
		return fc.returnResultsNow().next();
	}

	@Override
	public LeagueMembership findByMemberAndLeagueAtCurrentMoment(Long memberAutoId, Long leagueAutoId) {
		return findByMemberAndLeagueAndMoment(memberAutoId, leagueAutoId, timeProvider.getCurrentYear(),
				timeProvider.getCurrentMonth());
	}

	@Override
	public List<LeagueMembership> findUndeterminedByMinimumAutoIdAndMoment(int year, int month, int startFrom, int count) {
		RootFindCommand<LeagueMembership> fc = getOds().find().type(LeagueMembership.class).addFilter("year",
				FilterOperator.EQUAL, year).addFilter("month", FilterOperator.EQUAL, month).addFilter("resultCalculated",
				FilterOperator.EQUAL, false).startFrom(startFrom).fetchResultsBy(count);
		return getSegmentedList(fc, count);
	}

	@Override
	public List<LeagueMembership> getByLeagueAndRanking(Long leagueAutoId, long from, int count) {
		RootFindCommand<LeagueMembership> fc = getOds().find().type(LeagueMembership.class).addFilter("leagueAutoId",
				FilterOperator.EQUAL, leagueAutoId).addSort("accumulatedScore", SortDirection.DESCENDING).startFrom((int) from).fetchResultsBy(
				count);
		return getSegmentedList(fc, count);
	}

	@Override
	public long countByLeague(Long leagueAutoId) {
		RootFindCommand<LeagueMembership> fc = getOds().find().type(LeagueMembership.class).addFilter("leagueAutoId",
				FilterOperator.EQUAL, leagueAutoId);
		return fc.countResultsNow();
	}

}
