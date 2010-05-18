package org.seamoo.daos.twigImpl.matching;

import java.util.List;

import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.twigImpl.TwigGenericDaoImpl;
import org.seamoo.entities.matching.Match;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigMatchDaoImpl extends TwigGenericDaoImpl<Match, Long> implements MatchDao {

	@Override
	public List<Match> getRecentMatchesByLeague(Long leagueAutoId, long from, int count) {
		RootFindCommand<Match> fc = getOds().find().type(Match.class).addFilter("leagueAutoId", FilterOperator.EQUAL,
				leagueAutoId).addSort("endedMoment", SortDirection.DESCENDING).startFrom((int) from).fetchResultsBy(count);
		List<Match> results = Lists.newArrayList(fc.returnResultsNow());
		if (results.size() > count)
			return results.subList(0, count);
		return results;
	}

	@Override
	public long countByLeague(Long leagueAutoId) {
		RootFindCommand<Match> fc = getOds().find().type(Match.class).addFilter("leagueAutoId", FilterOperator.EQUAL,
				leagueAutoId);
		return fc.countResultsNow();
	}
}
