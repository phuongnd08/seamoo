package org.seamoo.utils;

import java.util.HashSet;
import java.util.Set;

public class SetBuilder {
	public static <T> Set<T> newSet(T... items) {
		HashSet<T> set = new HashSet<T>();
		for (T it : items)
			set.add(it);
		return set;
	}
}
