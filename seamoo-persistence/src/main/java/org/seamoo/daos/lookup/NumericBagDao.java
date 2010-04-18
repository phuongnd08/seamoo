package org.seamoo.daos.lookup;

import java.util.List;

import org.seamoo.daos.GenericDao;
import org.seamoo.lookup.NumericBag;

public interface NumericBagDao extends GenericDao<NumericBag, Long> {

	List<NumericBag> findByClassifier(String classifier);

}
