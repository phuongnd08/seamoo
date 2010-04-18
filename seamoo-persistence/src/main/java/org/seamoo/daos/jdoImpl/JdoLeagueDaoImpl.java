package org.seamoo.daos.jdoImpl;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.seamoo.daos.LeagueDao;
import org.seamoo.entities.League;
import org.seamoo.entities.Subject;

public class JdoLeagueDaoImpl extends JdoGenericDaoImpl<League, Long> implements LeagueDao {

	public List<League> getAllBySubjectId(Long subjectAutoId) {
		PersistenceManager pm = getPM();
		Query query = pm.newQuery(League.class);
		query.setFilter("subjectAutoId==mySubjectAutoId");
		query.declareParameters("long mySubjectAutoId");
		query.setOrdering("level ascending");
		return (List<League>) query.execute(subjectAutoId);
	}

	public List<League> getEnabledBySubjectId(Long subjectAutoId) {
		PersistenceManager pm = getPM();
		Query query = pm.newQuery(League.class);
		query.setFilter("subjectAutoId==mySubjectAutoId && enabled==myEnabled");
		query.declareParameters("long mySubjectAutoId, boolean myEnabled");
		query.setOrdering("level ascending");
		return (List<League>) query.execute(subjectAutoId, true);
	}
}
