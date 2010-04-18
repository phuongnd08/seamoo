package org.seamoo.daos.jdoImpl.lookup;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.seamoo.daos.jdoImpl.JdoGenericDaoImpl;
import org.seamoo.daos.lookup.NumericBagDao;
import org.seamoo.entities.Subject;
import org.seamoo.lookup.NumericBag;

public class JdoNumericBagDaoImpl extends JdoGenericDaoImpl<NumericBag, Long> implements NumericBagDao {
	public NumericBag findByKey(Long key) {
		PersistenceManager pm = getPM();
		try {
			return pm.detachCopy(pm.getObjectById(NumericBag.class, key));
		} catch (JDOObjectNotFoundException ex) {
			return null;
		}
	}

	public List<NumericBag> findByClassifier(String classifier) {
		// TODO Auto-generated method stub
		PersistenceManager pm = getPM();
		Query query = pm.newQuery(NumericBag.class);
		query.setFilter("classifier==myClassifier");
		query.declareParameters("String myClassifier");
		query.setOrdering("autoId ascending");
		return (List<NumericBag>) query.execute(classifier);
	}

}
