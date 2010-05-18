package org.seamoo.webapp;

import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.template.Configuration;

public class CustomizedFreemarkerServlet extends FreemarkerServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3079263156037673099L;

	@Override
	protected Configuration createConfiguration() {
		Configuration conf = super.createConfiguration();
		conf.setNumberFormat("0.#####");
		return conf;
	}
}
