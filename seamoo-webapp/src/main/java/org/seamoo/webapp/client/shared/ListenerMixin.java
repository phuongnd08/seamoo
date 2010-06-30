package org.seamoo.webapp.client.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class ListenerMixin<T> {
	public static interface Caller<L> {
		void perform(L listener);
	}

	List<T> listeners;

	public ListenerMixin() {
		listeners = new ArrayList<T>();
	}

	public void add(T listener) {
		listeners.add(listener);
	}

	public void remove(T listener) {
		listeners.remove(listener);
	}

	public void each(Caller<T> caller) {
		for (T listener : listeners)
			caller.perform(listener);
	}

	public ClickHandler getClickHandler(final Caller<T> caller) {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				each(caller);
			}
		};
	}
}
