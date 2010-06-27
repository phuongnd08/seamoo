package org.seamoo.utils;

public class UrlParameter {
	private String name;
	private String value;

	public UrlParameter(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("%s=%s", name, StringBuilder.urlEncode(value, "utf-8"));
	}
}
