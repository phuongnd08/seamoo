package org.seamoo.webapp.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.SiteSettingDao;
import org.seamoo.daos.SubjectDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.League;
import org.seamoo.entities.Subject;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.utils.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
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

	@Autowired
	QuestionDao questionDao;

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

	private void performInstall() throws IOException {
		Subject[] subjects = installSubjects();

		League[] leagues = installLeagues(subjects[0]);

		installQuestions(leagues[0]);
	}

	private Subject[] installSubjects() {
		Subject english = new Subject();
		{
			english.setName("English");
			english.setDescription("Thể hiện kĩ năng Anh ngữ của bạn về từ vựng, ngữ pháp và phát âm");
			english.setLogoUrl("/images/subjects/english.png");
			english.setEnabled(true);
			english.setAddedTime(new Date());
		}

		Subject maths = new Subject();
		{
			maths.setName("Toán học");
			maths.setDescription("Thể hiện sự hiểu biết cùng khả năng suy luận toán học của bạn");
			maths.setLogoUrl("/images/subjects/math.png");
			maths.setEnabled(true);
			maths.setAddedTime(new Date());
		}

		Subject history = new Subject();
		{
			history.setName("Lịch sử");
			history.setDescription("Thể hiện sự am tường lịch sử của bạn");
			history.setLogoUrl("/images/subjects/history.png");
			history.setEnabled(false);
			history.setAddedTime(new Date());
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

	final int QUESTION_NUMBER = 130;
	final String QUESTION_RESOURCE_PATH = "classpath:BasicEnglish.QnA";

	private void installQuestions(League league) throws IOException {
		ResourceLoader loader = new DefaultResourceLoader();
		InputStream questionStream = null;
		BufferedReader questionReader = null;
		try {
			questionStream = loader.getResource(QUESTION_RESOURCE_PATH).getInputStream();
			questionReader = new BufferedReader(new InputStreamReader(questionStream));
			int i = 0;
			List<Question> qs = new ArrayList<Question>();
			while (i < QUESTION_NUMBER && questionReader.ready()) {
				String s = questionReader.readLine();
				String[] data = s.split("\\|");
				String question = data[0];
				String[] choices = Arrays.copyOfRange(data, 1, data.length);
				qs.add(createQuestion(question, choices));
				i++;
			}
			questionDao.persist(qs.toArray(new Question[qs.size()]));
		} finally {
			questionReader.close();
			questionStream.close();
		}
	}

	Random rndGenerator = new Random();

	private Question createQuestion(String question, String[] choices) {
		// TODO Auto-generated method stub
		Question q = new Question();
		MultipleChoicesQuestionRevision r = new MultipleChoicesQuestionRevision();
		r.setContent(question);
		List<String> lists = new ArrayList<String>();
		for (String s : choices)
			lists.add(s);
		while (lists.size() > 0) {
			// pick a random choice
			int index = rndGenerator.nextInt(lists.size());
			String choice = lists.get(index);
			lists.remove(index);
			// add it to the question
			QuestionChoice c = new QuestionChoice();
			c.setCorrect(choice.startsWith("*"));
			c.setContent(choice.substring(c.isCorrect() ? 1 : 0));
			r.addChoice(c);
		}
		q.addAndSetAsCurrentRevision(r);
		return q;
	}
}
