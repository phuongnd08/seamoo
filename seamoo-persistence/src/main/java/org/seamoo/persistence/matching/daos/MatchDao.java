package org.seamoo.persistence.matching.daos;

import java.util.List;

import org.seamoo.entities.matching.Match;
import org.seamoo.persistence.daos.GenericDao;

public interface MatchDao extends GenericDao<Match, Long> {

	List<Match> getAllActiveMatches();

}
