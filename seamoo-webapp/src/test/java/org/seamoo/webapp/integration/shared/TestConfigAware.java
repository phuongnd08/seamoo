package org.seamoo.webapp.integration.shared;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

public class TestConfigAware {

	protected static Properties testProperties;

	public TestConfigAware() {
		super();
	}

	protected String getServerBase() {
		return testProperties.getProperty("serverBase");
	}

	static {
		testProperties = new Properties();
		try {
			ResourceLoader rLoader = new DefaultResourceLoader();
			testProperties.load(rLoader.getResource("classpath:it-test.properties").getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}