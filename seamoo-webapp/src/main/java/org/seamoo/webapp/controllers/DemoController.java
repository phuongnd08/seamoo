package org.seamoo.webapp.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.seamoo.entities.League;
import org.seamoo.entities.Subject;
import org.seamoo.persistence.daos.LeagueDao;
import org.seamoo.persistence.daos.SiteSettingDao;
import org.seamoo.persistence.daos.SubjectDao;
import org.seamoo.utils.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	SiteSettingDao siteSettingDao;

	@Autowired
	SubjectDao subjectDAO;

	@Autowired
	LeagueDao leagueDao;

	@RequestMapping("/install-sample")
	public void installSample(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		boolean sampleInstalled = Converter.toBoolean(siteSettingDao.getSetting("sampleInstalled"));
		if (!sampleInstalled) {
			response.getWriter().println("Install Sample");
			performInstall();
			siteSettingDao.assignSetting("sampleInstalled", Converter.toString(true));
		} else {
			response.getWriter().println("Error: Sample Installed");
		}
	}

	private void performInstall() {
		Subject[] subjects = installSubjects();

		League[] leagues = installLeagues(subjects[0]);
	}

	private Subject[] installSubjects() {
		Subject english = new Subject();
		{
			english.setName("English");
			english.setDescription("Thể hiện kĩ năng Anh ngữ của bạn về từ vựng, ngữ pháp và phát âm");
			english.setLogoUrl("/images/subjects/english.png");
			english.setEnabled(true);
		}

		Subject maths = new Subject();
		{
			maths.setName("Toán học");
			maths.setDescription("Thể hiện sự hiểu biết cùng khả năng suy luận toán học của bạn");
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

		Subject[] subjects = new Subject[] { english, maths, history };
		subjectDAO.persist(subjects);
		return subjects;
	}

	private League[] installLeagues(Subject subject) {
		League englishAmateur = new League();
		{
			englishAmateur.setName("Giải nghiệp dư");
			englishAmateur.setDescription("Dành cho những người lần đầu tham gia ");
			englishAmateur.setLogoUrl("/images/leagues/eng-amateur.png");
			englishAmateur.setLevel(0);
			englishAmateur.setEnabled(true);
		}

		League englishChick = new League();
		{
			englishChick.setName("Giải gà con");
			englishChick.setDescription("Dành cho những đấu thủ đã thể hiện được mình ở Giải nghiệp dư ");
			englishChick.setLogoUrl("/images/leagues/eng-league-2.png");
			englishChick.setLevel(1);
			englishChick.setEnabled(true);
		}

		League englishCock = new League();
		{
			englishCock.setName("Giải gà chọi");
			englishCock.setDescription("Dành cho những đấu thủ đẳng cấp ");
			englishCock.setLogoUrl("/images/leagues/eng-league-1.png");
			englishCock.setLevel(2);
			englishCock.setEnabled(true);
		}

		League englishEagle = new League();
		{
			englishEagle.setName("Giải đại bàng");
			englishEagle.setDescription("Dành cho những đấu thủ đẳng cấp cao");
			englishEagle.setLogoUrl("/images/leagues/eng-pro-league.png");
			englishEagle.setLevel(3);
			englishEagle.setEnabled(true);
		}
		League[] leagues = new League[] { englishAmateur, englishChick, englishCock, englishEagle };
		for (League l : leagues)
			l.setSubjectAutoId(subject.getAutoId());
		leagueDao.persist(leagues);
		return leagues;
	}
}
