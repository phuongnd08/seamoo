package org.seamoo.persistence.daos.jdo;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.seamoo.entities.Subject;
import org.seamoo.persistence.daos.SubjectDao;

public class JdoSubjectDaoImpl extends JdoGenericDaoImpl<Subject, Long> implements SubjectDao {

	public List<Subject> getEnabledSubjects() {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPM();
		Query query = pm.newQuery(Subject.class);
		query.setFilter("enabled==myEnabled");
		query.declareParameters("boolean myEnabled");
		query.setOrdering("addedTime ascending");
		return (List<Subject>) query.execute(true);
	}

	@Override
	public List<Subject> getAll() {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPM();
		Query query = pm.newQuery(Subject.class);
		query.setOrdering("addedTime ascending");
		return (List<Subject>) query.execute();
	}

}
