package org.seamoo.daos.twigImpl.matching;

import java.util.List;

import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.twigImpl.TwigGenericDaoImpl;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigMatchDaoImpl extends TwigGenericDaoImpl<Match, Long> implements MatchDao {

	@Override
	public List<Match> getAllActiveMatches() {
		// TODO Auto-generated method stub
		return null;
	}

}
