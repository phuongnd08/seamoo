package org.seamoo.daos.twigImpl;

import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

public class ObjectDatastoreProvider {
	public static final ObjectDatastoreProvider DEFAULT = new ObjectDatastoreProvider();
	private ThreadLocal<ObjectDatastore> objectDataStore = new ThreadLocal<ObjectDatastore>();

	public ObjectDatastore getObjectDataStore() {
		ObjectDatastore ods = objectDataStore.get();
		if (ods==null){
			ods = new AnnotationObjectDatastore();
			objectDataStore.set(ods);
		}
		return ods;
	}
}
