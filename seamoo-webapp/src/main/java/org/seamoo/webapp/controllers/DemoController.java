package org.seamoo.webapp.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.seamoo.persistence.SiteSettingDAO;
import org.seamoo.utils.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	SiteSettingDAO siteSettingDAO;

	@RequestMapping("/install-sample")
	public void installSample(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		boolean sampleInstalled = Converter.toBoolean(siteSettingDAO
				.getSetting("sampleInstalled"));
		if (!sampleInstalled) {
			response.getWriter().println("Install Sample");
			siteSettingDAO.assignSetting("sampleInstalled", Converter
					.toString(true));
		} else {
			response.getWriter().println("Error: Sample Installed");
		}
	}
}
