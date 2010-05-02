package org.seamoo.daos.ofyImpl.lookup;

import java.util.List;

import org.seamoo.daos.lookup.NumericBagDao;
import org.seamoo.daos.ofyImpl.OfyGenericDaoImpl;
import org.seamoo.lookup.NumericBag;

import com.google.common.collect.Lists;
import com.googlecode.objectify.Query;

public class OfyNumericBagDaoImpl extends OfyGenericDaoImpl<NumericBag, Long> implements NumericBagDao {
	public NumericBag findByClassifier(String classifier) {
		// TODO Auto-generated method stub
		Query<NumericBag> q = getOfy().query(NumericBag.class).filter("classifier =", classifier);
		return q.get();
	}

}
