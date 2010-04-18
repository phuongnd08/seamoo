package org.seamoo.daos.twigImpl;

import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

public class TOD {
	static ObjectDatastore objectDatastore;

	public static synchronized ObjectDatastore getObjectDataStore() {
		if (objectDatastore == null) {
			objectDatastore = new AnnotationObjectDatastore();
		}
		return objectDatastore;
	}
}
