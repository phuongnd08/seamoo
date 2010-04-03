package org.seamoo.webapp.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.seamoo.entities.Subject;
import org.seamoo.persistence.SiteSettingDAO;
import org.seamoo.persistence.SubjectDAO;
import org.seamoo.utils.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	SiteSettingDAO siteSettingDAO;

	@Autowired
	SubjectDAO subjectDAO;

	@RequestMapping("/install-sample")
	public void installSample(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		boolean sampleInstalled = Converter.toBoolean(siteSettingDAO
				.getSetting("sampleInstalled"));
		if (!sampleInstalled) {
			response.getWriter().println("Install Sample");
			performInstall();
			siteSettingDAO.assignSetting("sampleInstalled", Converter
					.toString(true));
		} else {
			response.getWriter().println("Error: Sample Installed");
		}
	}

	private void performInstall() {
		Subject english = new Subject();
		{
			english.setName("English");
			english
					.setDescription("Thể hiện kĩ năng Anh ngữ của bạn về từ vựng, ngữ pháp và phát âm");
			english.setLogoUrl("/images/subjects/english.png");
			english.setEnabled(true);
		}

		Subject maths = new Subject();
		{
			maths.setName("Toán học");
			maths
					.setDescription("Thể hiện sự hiểu biết cùng khả năng suy luận toán học của bạn");
			maths.setLogoUrl("/images/subjects/math.png");
			maths.setEnabled(true);
		}

		Subject history = new Subject();
		{
			history.setName("Lịch sử");
			history.setDescription("Thể hiện sự am tường lịch sử của bạn");
			history.setLogoUrl("/images/subjects/history.png");
			history.setEnabled(false);
		}

		subjectDAO.persist(new Subject[] { english, maths, history });
	}
}
