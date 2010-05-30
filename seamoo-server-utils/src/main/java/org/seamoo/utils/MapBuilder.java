package org.seamoo.utils;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder {
	public static Map toFreeMarkerMap(Map original) {
		Map freeMarkerMap = new HashMap();
		for (Object key : original.keySet()) {
			freeMarkerMap.put(String.valueOf(key), original.get(key));
		}
		return freeMarkerMap;

	}
}
