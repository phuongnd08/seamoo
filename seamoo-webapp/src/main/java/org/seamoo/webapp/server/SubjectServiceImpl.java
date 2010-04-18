package org.seamoo.webapp.server;

import java.util.ArrayList;
import java.util.List;

import org.seamoo.daos.SubjectDao;
import org.seamoo.entities.Subject;
import org.seamoo.webapp.client.admin.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SubjectServiceImpl extends RemoteServiceServlet implements SubjectService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5571705768473561343L;
	@Autowired
	SubjectDao subjectDao;

	@Override
	public void delete(Subject subject) {
		// TODO Auto-generated method stub
		subjectDao.delete(subject);

	}

	@Override
	public List<Subject> getAll() {
		// TODO Auto-generated method stub
		List<Subject> result = subjectDao.getAll();
		return new ArrayList<Subject>(result);
	}

	@Override
	public Subject save(Subject subject) {
		// TODO Auto-generated method stub
		subjectDao.persist(subject);
		return subject;
	}

}
