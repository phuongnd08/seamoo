package org.seamoo.utils;

import java.util.Iterator;

public interface ResourceIterator<E> extends Iterator<E>, Iterable<E>{

	void close();
}
