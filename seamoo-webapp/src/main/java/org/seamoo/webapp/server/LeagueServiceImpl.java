package org.seamoo.webapp.server;

import java.util.ArrayList;
import java.util.List;

import org.seamoo.entities.League;
import org.seamoo.persistence.daos.LeagueDao;
import org.seamoo.persistence.daos.SubjectDao;
import org.seamoo.webapp.client.admin.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LeagueServiceImpl extends RemoteServiceServlet implements LeagueService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5571705768473561343L;
	@Autowired
	LeagueDao leagueDao;
	SubjectDao subjectDao;

	@Override
	public void delete(League league) {
		// TODO Auto-generated method stub
		leagueDao.delete(league);

	}

	@Override
	public List<League> getAll(Long subjectId) {
		// TODO Auto-generated method stub
		List<League> result = leagueDao.getAllBySubjectId(subjectId);
		return new ArrayList<League>(result);
	}

	@Override
	public League save(Long subjectId, League league) {
		// TODO Auto-generated method stub
		league.setSubjectAutoId(subjectId);
		leagueDao.persist(league);
		return league;
	}
}
