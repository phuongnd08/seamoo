package org.seamoo.daos.twigImpl;

import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

public class ObjectDatastoreProvider {
	public static final ObjectDatastoreProvider DEFAULT = new ObjectDatastoreProvider();
	ThreadLocal<ObjectDatastore> local = new ThreadLocal<ObjectDatastore>() {
		@Override
		protected ObjectDatastore initialValue() {
			return null;
		}
	};

	public ObjectDatastore getObjectDataStore() {
		ObjectDatastore ods = local.get();
		if (ods == null) {
			ods = new AnnotationObjectDatastore();
			local.set(ods);
		}
		return ods;
	}
}
