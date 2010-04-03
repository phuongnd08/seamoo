package org.seamoo.webapp.client.uimocker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickHandler;

public class MockedWidgetHelper {
	private Map<String, List<Object>> handlersMap;
	private Map<String, Object> fieldMap;

	public MockedWidgetHelper() {
		handlersMap = new HashMap<String, List<Object>>();
		fieldMap = new HashMap<String, Object>();
		fieldMap.put("visible", true);
	}

	private void addHandler(String name, Object handler) {
		if (!handlersMap.containsKey(name)) {
			handlersMap.put(name, new ArrayList<Object>());
		}
		handlersMap.get(name).add(handler);
		// initialize some default field
	}

	public void addClickHandler(ClickHandler handler) {
		addHandler("click", handler);
	}

	public void click() {
		if (handlersMap.containsKey("click")) {
			for (Object handler : handlersMap.get("click")) {
				((ClickHandler) handler).onClick(null);
			}
		}
	}

	public void setField(String name, Object value) {
		fieldMap.put(name, value);
	}

	public Object getField(String name) {
		if (fieldMap.containsKey(name))
			return fieldMap.get(name);
		else
			return null;
	}

	private String value = null;
}
