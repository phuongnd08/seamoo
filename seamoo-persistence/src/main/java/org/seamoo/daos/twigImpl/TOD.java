package org.seamoo.daos.twigImpl;

import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

public class TOD {
	static ThreadLocal<ObjectDatastore> local = new ThreadLocal<ObjectDatastore>() {
		@Override
		protected ObjectDatastore initialValue() {
			return null;
		}
	};

	static ObjectDatastore objectDatastore;

	public static ObjectDatastore getObjectDataStore() {
		ObjectDatastore ods = local.get();
		if (ods == null) {
			ods = new AnnotationObjectDatastore();
			local.set(ods);
		}
		return ods;
	}
}
