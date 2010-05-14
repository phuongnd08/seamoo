package org.seamoo.daos.twigImpl;

import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

public class ObjectDatastoreProvider {
	public static final ObjectDatastoreProvider DEFAULT = new ObjectDatastoreProvider();

	public ObjectDatastore getObjectDataStore() {
		return new AnnotationObjectDatastore();
	}
}
