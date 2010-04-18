package org.seamoo.daos;

import java.util.List;

import org.seamoo.entities.League;
import org.seamoo.entities.Subject;

public interface LeagueDao extends GenericDao<League, Long> {
	public List<League> getAllBySubjectId(Long subjectId);

	public List<League> getEnabledBySubjectId(Long subjectId);
}
