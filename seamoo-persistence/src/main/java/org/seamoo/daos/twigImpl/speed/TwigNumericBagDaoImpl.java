package org.seamoo.daos.twigImpl.speed;

import org.seamoo.daos.speed.NumericBagDao;
import org.seamoo.daos.twigImpl.TwigGenericDaoImpl;
import org.seamoo.lookup.NumericBag;
import org.seamoo.utils.SetBuilder;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.vercer.engine.persist.FindCommand.RootFindCommand;

public class TwigNumericBagDaoImpl extends TwigGenericDaoImpl<NumericBag, Long> implements NumericBagDao {
	public static final int NUMERIC_CACHE_TIME = 60 * 1000;

	public TwigNumericBagDaoImpl() {
		super(SetBuilder.newSet("classifier"), new FieldGetter<NumericBag>() {

			@Override
			public Object getField(NumericBag object, String field) {
				if (field.equals("classifier"))
					return object.getClassifier();
				return null;
			}

			@Override
			public Object getKey(NumericBag object) {
				return object.getAutoId();
			}
		}, NUMERIC_CACHE_TIME);
	}

	public NumericBag findByClassifier(String classifier) {
		// TODO Auto-generated method stub
		RootFindCommand<NumericBag> fc = getOds().find().type(NumericBag.class).addFilter("classifier", FilterOperator.EQUAL,
				classifier);
		if (fc.countResultsNow() == 0)
			return null;
		return fc.returnResultsNow().next();
	}

}
