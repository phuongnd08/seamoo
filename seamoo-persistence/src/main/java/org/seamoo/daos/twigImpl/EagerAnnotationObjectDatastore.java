package org.seamoo.daos.twigImpl;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.vercer.engine.persist.standard.StrategyObjectDatastore;

public class EagerAnnotationObjectDatastore extends StrategyObjectDatastore{
	public EagerAnnotationObjectDatastore()
	{
		super(DatastoreServiceFactory.getDatastoreService(), new EagerAnnotationStrategy(true, 0));
	}
	
}
