package org.seamoo.daos.ofyImpl;

import java.util.List;

import javax.jdo.PersistenceManager;

import org.seamoo.daos.LeagueDao;
import org.seamoo.entities.League;

import com.google.common.collect.Lists;
import com.googlecode.objectify.Query;

public class OfyLeagueDaoImpl extends OfyGenericDaoImpl<League, Long> implements LeagueDao {

	public List<League> getAllBySubjectId(Long subjectAutoId) {
		Query<League> q = getOfy().query(League.class).filter("subjectAutoId =", subjectAutoId);
		return Lists.newArrayList(q.fetch());
	}

	public List<League> getEnabledBySubjectId(Long subjectAutoId) {
		Query<League> q = getOfy().query(League.class).filter("subjectAutoId =", subjectAutoId).filter("enabled =", true);
		return Lists.newArrayList(q.fetch());
	}
}
