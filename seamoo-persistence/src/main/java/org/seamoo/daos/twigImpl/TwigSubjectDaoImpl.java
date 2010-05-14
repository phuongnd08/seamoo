package org.seamoo.daos.twigImpl;

import java.util.List;

import org.seamoo.daos.SubjectDao;
import org.seamoo.entities.Subject;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Query;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigSubjectDaoImpl extends TwigGenericDaoImpl<Subject, Long> implements SubjectDao {

	public List<Subject> getEnabledSubjects() {
		// TODO Auto-generated method stub
		RootFindCommand<Subject> fc = newOds().find().type(Subject.class).addFilter("enabled", FilterOperator.EQUAL, true).addSort(
				"addedTime", SortDirection.ASCENDING);
		return Lists.newArrayList(fc.returnResultsNow());
	}

	@Override
	public List<Subject> getAll() {
		// TODO Auto-generated method stub
		RootFindCommand<Subject> fc = newOds().find().type(Subject.class).addSort("addedTime", SortDirection.ASCENDING);
		return Lists.newArrayList(fc.returnResultsNow());
	}

}
