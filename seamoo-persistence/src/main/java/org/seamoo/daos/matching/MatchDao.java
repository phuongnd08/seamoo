package org.seamoo.daos.matching;

import java.util.List;

import org.seamoo.daos.GenericDao;
import org.seamoo.entities.matching.Match;

public interface MatchDao extends GenericDao<Match, Long> {

	List<Match> getAllActiveMatches();

}
