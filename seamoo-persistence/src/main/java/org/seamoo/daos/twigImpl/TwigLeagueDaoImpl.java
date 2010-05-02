package org.seamoo.daos.twigImpl;

import java.util.List;

import org.seamoo.daos.LeagueDao;
import org.seamoo.entities.League;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.common.collect.Lists;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigLeagueDaoImpl extends TwigGenericDaoImpl<League, Long> implements LeagueDao {

	public List<League> getAllBySubjectId(Long subjectAutoId) {
		RootFindCommand<League> fc = getOds().find().type(League.class).addFilter("subjectAutoId", FilterOperator.EQUAL,
				subjectAutoId);
		return Lists.newArrayList(fc.returnResultsNow());
	}

	public List<League> getEnabledBySubjectId(Long subjectAutoId) {
		RootFindCommand<League> fc = getOds().find().type(League.class).addFilter("subjectAutoId", FilterOperator.EQUAL,
				subjectAutoId).addFilter("enabled", FilterOperator.EQUAL, true);
		return Lists.newArrayList(fc.returnResultsNow());
	}

	@Override
	public League findBySubjectIdAndLevel(Long subjectAutoId, int level) {
		RootFindCommand<League> fc = getOds().find().type(League.class).addFilter("subjectAutoId", FilterOperator.EQUAL,
				subjectAutoId).addFilter("level", FilterOperator.EQUAL, level);
		if (fc.countResultsNow() == 0)
			return null;
		return fc.returnResultsNow().next();
	}
}
