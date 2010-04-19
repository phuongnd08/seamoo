package org.seamoo.daos.ofyImpl;

import java.util.List;

import org.seamoo.daos.SubjectDao;
import org.seamoo.entities.Subject;

import com.google.common.collect.Lists;
import com.googlecode.objectify.Query;

public class OfySubjectDaoImpl extends OfyGenericDaoImpl<Subject, Long> implements SubjectDao {

	public List<Subject> getEnabledSubjects() {
		// TODO Auto-generated method stub
		Query<Subject> q = getOfy().query(Subject.class).filter("enabled =", true);
		q.order("addedTime");
		return Lists.newArrayList(q.fetch());
	}

	@Override
	public List<Subject> getAll() {
		// TODO Auto-generated method stub
		Query<Subject> q = getOfy().query(Subject.class);
		q.order("addedTime");
		return Lists.newArrayList(q.fetch());
	}

}
