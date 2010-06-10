package org.seamoo.daos.twigImpl;

import com.vercer.engine.persist.ObjectDatastore;
import com.vercer.engine.persist.annotation.AnnotationObjectDatastore;

public class ObjectDatastoreProvider {
	public static final ObjectDatastoreProvider DEFAULT = new ObjectDatastoreProvider();
	private ThreadLocal<ObjectDatastore> objectDataStore = new ThreadLocal<ObjectDatastore>();
	private ThreadLocal<ObjectDatastore> eagerObjectDataStore = new ThreadLocal<ObjectDatastore>();
	
	public ObjectDatastore getObjectDataStore() {
		ObjectDatastore ods = objectDataStore.get();
		if (ods==null){
			ods = new AnnotationObjectDatastore();
			objectDataStore.set(ods);
		}
		return ods;
	}
	
	public ObjectDatastore getEagerObjectDatastore(){
		ObjectDatastore ods = eagerObjectDataStore.get();
		if (ods==null){
			ods = new EagerAnnotationObjectDatastore();
			eagerObjectDataStore.set(ods);
		}
		return ods;
	}
}
