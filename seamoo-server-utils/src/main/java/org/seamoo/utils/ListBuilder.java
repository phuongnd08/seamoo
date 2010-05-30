package org.seamoo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListBuilder {

	public static <T, Tid> List<T> extractList(Map<Tid, T> map, List<Tid> idList) {
		List<T> results = new ArrayList<T>();
		for (Tid id : idList) {
			results.add(map.get(id));
		}
		return results;
	}

}
