package org.seamoo.daos.twigImpl.lookup;

import java.util.List;

import org.seamoo.daos.lookup.NumericBagDao;
import org.seamoo.daos.twigImpl.TwigGenericDaoImpl;
import org.seamoo.lookup.NumericBag;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Query;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigNumericBagDaoImpl extends TwigGenericDaoImpl<NumericBag, Long> implements NumericBagDao {
	public List<NumericBag> findByClassifier(String classifier) {
		// TODO Auto-generated method stub
		RootFindCommand<NumericBag> fc = getOds().find().type(NumericBag.class).addFilter("classifier", FilterOperator.EQUAL,
				classifier);
		return Lists.newArrayList(fc.returnResultsNow());
	}

}
