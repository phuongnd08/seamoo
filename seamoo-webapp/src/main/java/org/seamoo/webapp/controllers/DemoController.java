package org.seamoo.webapp.controllers;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.SiteSettingDao;
import org.seamoo.daos.SubjectDao;
import org.seamoo.daos.installation.BundleDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.League;
import org.seamoo.entities.Subject;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.installation.Bundle;
import org.seamoo.utils.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;

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

	@Autowired
	BundleDao bundleDao;

	@RequestMapping("/install-leagues")
	public void installLeagues(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		boolean sampleInstalled = Converter.toBoolean(siteSettingDao.getSetting("sampleInstalled"));
		if (!sampleInstalled) {
			response.getWriter().println("Leagues is being installed<br/>");
			Subject[] subjects = internalInstallSubjects();
			League[] leagues = internalInstallLeagues(subjects[0]);
			siteSettingDao.assignSetting("sampleInstalled", Converter.toString(true));
			response.getWriter().println("Install done!<br/>");
		} else {
			response.getWriter().println("Error: Sample Installed");
		}
	}

	private Subject[] internalInstallSubjects() {
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

	private League[] internalInstallLeagues(Subject subject) {
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
	final String DATA_INDEX_PATH = "classpath:data.index";
	final long MAX_DURATION = 22000;// only run an installation for 22 seconds

	@RequestMapping("/install-questions")
	private void installQuestions(HttpServletResponse response,
			@RequestParam(required = false, value = "bundleName") String bundleName,
			@RequestParam(required = false, value = "finished") Long finished,
			@RequestParam(required = false, value = "leagueId") Long leagueId) throws IOException {

		if (bundleName != null) {
			Long[] newFinished = new Long[1];
			boolean done = installPackage(bundleName, finished, newFinished, MAX_DURATION, leagueDao.findByKey(leagueId));
			if (done) {
				Bundle p = new Bundle(bundleName, newFinished[0], true);
				bundleDao.persist(p);
			} else {
				TaskOptions taskOptions = TaskOptions.Builder.url("/demo/install-questions").param("bundleName", bundleName).param(
						"finished", newFinished[0].toString()).param("leagueId", leagueId.toString());

				Queue q = QueueFactory.getDefaultQueue();
				q.add(taskOptions);
			}
		} else {
			List<Bundle> allInstalledBundles = bundleDao.getAll();
			Set<String> allInstalledBundlesName = new HashSet<String>();
			for (Bundle b : allInstalledBundles)
				allInstalledBundlesName.add(b.getName());
			ResourceLoader loader = new DefaultResourceLoader();
			InputStream stream = loader.getResource(DATA_INDEX_PATH).getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			while (reader.ready()) {
				String s = reader.readLine();
				String[] data = s.split("\\|");
				String buName = data[0];
				Long leId = Converter.toLong(data[1]);
				if (!allInstalledBundles.contains(buName)) {
					TaskOptions taskOptions = TaskOptions.Builder.url("/demo/install-questions").param("bundleName", buName).param(
							"finished", "0").param("leagueId", leId.toString());

					Queue q = QueueFactory.getDefaultQueue();
					q.add(taskOptions);
					break;
				}
			}
			reader.close();
			stream.close();
		}
	}

	long QUESTION_BATCH_SIZE = 20;

	private boolean installPackage(String bundleName, Long finished, Long[] newFinished, long maxDuration, League league)
			throws IOException {
		ResourceLoader loader = new DefaultResourceLoader();
		InputStream stream = null;
		BufferedReader reader = null;
		stream = loader.getResource(bundleName).getInputStream();
		reader = new BufferedReader(new InputStreamReader(stream));
		long passed = 0;
		if (finished != null)
			while (passed < finished && reader.ready()) {
				reader.readLine();
				passed++;
			}
		boolean done = false;
		long currentMilliseconds = System.currentTimeMillis();
		newFinished[0] = passed;
		while (System.currentTimeMillis() - currentMilliseconds < maxDuration) {
			int i = 0;
			List<Question> qs = new ArrayList<Question>();
			while (i < QUESTION_BATCH_SIZE && reader.ready()) {
				String s = reader.readLine();
				String[] data = s.split("\\|");
				String question = data[0];
				String[] choices = Arrays.copyOfRange(data, 1, data.length);
				qs.add(createQuestion(league, question, choices));
				i++;
			}
			questionDao.persist(qs.toArray(new Question[qs.size()]));
			newFinished[0] += qs.size();
			if (!reader.ready()) {// end of stream
				done = true;
				break;
			}
		}
		reader.close();
		stream.close();
		return done;
	}

	Random rndGenerator = new Random();

	private Question createQuestion(League league, String question, String[] choices) {
		// TODO Auto-generated method stub
		Question q = new Question();
		q.setLeagueAutoId(league.getAutoId());
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
