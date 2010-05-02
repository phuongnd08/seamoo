package org.seamoo.daos;

import java.util.List;

import org.seamoo.entities.League;
import org.seamoo.entities.Subject;

public interface LeagueDao extends GenericDao<League, Long> {
	List<League> getAllBySubjectId(Long subjectId);

	List<League> getEnabledBySubjectId(Long subjectId);

	League findBySubjectIdAndLevel(Long subjectAutoId, int i);
}
