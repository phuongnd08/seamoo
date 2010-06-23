package org.seamoo.daos.speed;

import java.util.List;

import org.seamoo.daos.GenericDao;
import org.seamoo.lookup.NumericBag;

public interface NumericBagDao extends GenericDao<NumericBag, Long> {

	NumericBag findByClassifier(String classifier);

}
