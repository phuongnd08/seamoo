package org.seamoo.daos.twigImpl;

import java.util.HashSet;
import java.util.List;

import org.seamoo.daos.SubjectDao;
import org.seamoo.daos.twigImpl.TwigGenericDaoImpl.FieldGetter;
import org.seamoo.entities.League;
import org.seamoo.entities.Subject;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Query;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigSubjectDaoImpl extends TwigGenericDaoImpl<Subject, Long> implements SubjectDao {
	
	static final int SUBJECT_CACHE_PERIOD = 60 * 60 * 1000;

	public TwigSubjectDaoImpl() {
		super(new HashSet<String>(), new FieldGetter<Subject>() {

			@Override
			public Object getField(Subject object, String field) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Object getKey(Subject object) {
				return object.getAutoId();
			}
		}, SUBJECT_CACHE_PERIOD);
	}

	public List<Subject> getEnabledSubjects() {
		// TODO Auto-generated method stub
		RootFindCommand<Subject> fc = getOds().find().type(Subject.class).addFilter("enabled", FilterOperator.EQUAL, true).addSort(
				"addedTime", SortDirection.ASCENDING);
		return Lists.newArrayList(fc.returnResultsNow());
	}

	@Override
	public List<Subject> getAll() {
		// TODO Auto-generated method stub
		RootFindCommand<Subject> fc = getOds().find().type(Subject.class).addSort("addedTime", SortDirection.ASCENDING);
		return Lists.newArrayList(fc.returnResultsNow());
	}

}
