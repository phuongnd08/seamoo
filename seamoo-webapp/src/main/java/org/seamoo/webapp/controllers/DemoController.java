package org.seamoo.webapp.controllers;

import java.io.IOException;
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
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.League;
import org.seamoo.entities.Subject;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.installation.Bundle;
import org.seamoo.utils.ResourceIterator;
import org.seamoo.utils.ResourceIteratorProvider;
import org.seamoo.utils.TimeProvider;
import org.seamoo.utils.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	MatchDao matchDao;
	
	@Autowired
	ResourceIteratorProvider resourceIteratorProvider;
	
	@Autowired
	TimeProvider timeProvider;

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
			englishAmateur.setAlias("giai-nghiep-du");
			englishAmateur.setDescription("Dành cho những người lần đầu tham gia ");
			englishAmateur.setLogoUrl("/images/leagues/eng-amateur.png");
			englishAmateur.setLevel(0);
			englishAmateur.setEnabled(true);
		}

		League englishChick = new League();
		{
			englishChick.setName("Giải gà con");
			englishChick.setAlias("giai-ga-con");
			englishChick.setDescription("Dành cho những đấu thủ đã thể hiện được mình ở Giải nghiệp dư ");
			englishChick.setLogoUrl("/images/leagues/eng-league-2.png");
			englishChick.setLevel(1);
			englishChick.setEnabled(true);

		}

		League englishCock = new League();
		{
			englishCock.setName("Giải gà chọi");
			englishCock.setAlias("giai-ga-choi");
			englishCock.setDescription("Dành cho những đấu thủ đẳng cấp ");
			englishCock.setLogoUrl("/images/leagues/eng-league-1.png");
			englishCock.setLevel(2);
			englishCock.setEnabled(true);

		}

		League englishEagle = new League();
		{
			englishEagle.setName("Giải đại bàng");
			englishEagle.setAlias("giai-dai-bang");
			englishEagle.setDescription("Dành cho những đấu thủ đẳng cấp cao");
			englishEagle.setLogoUrl("/images/leagues/eng-pro-league.png");
			englishEagle.setLevel(3);
			englishEagle.setEnabled(true);
		}
		League[] leagues = new League[] { englishAmateur, englishChick, englishCock, englishEagle };
		for (League l : leagues) {
			l.setSubjectAutoId(subject.getAutoId());
		}
		leagueDao.persist(leagues);
		return leagues;
	}

	public static final String DATA_INDEX_PATH = "classpath:data.index";
	public static final long MAX_DURATION = 22000;// only run an installation for 22 seconds

	@RequestMapping("/install-questions")
	public void installQuestions(HttpServletResponse response,
			@RequestParam(required = false, value = "bundleName") String bundleName,
			@RequestParam(required = false, value = "finished") Long finished,
			@RequestParam(required = false, value = "leagueId") Long leagueId) throws IOException {

		internalInstallQuestions(bundleName, finished, leagueId, QueueFactory.getDefaultQueue());
	}

	void internalInstallQuestions(String bundleName, Long finished, Long leagueId, Queue taskQueue)
			throws IOException {
		if (bundleName != null) {
			Long[] newFinished = new Long[1];
			boolean done = installPackage(bundleName, finished, newFinished, MAX_DURATION, leagueDao.findByKey(leagueId));
			if (done) {
				Bundle p = new Bundle(bundleName, newFinished[0], true);
				bundleDao.persist(p);
				TaskOptions taskOptions = TaskOptions.Builder.url("/demo/install-questions");

				taskQueue.add(taskOptions);
			} else {
				TaskOptions taskOptions = TaskOptions.Builder.url("/demo/install-questions").param("bundleName", bundleName).param(
						"finished", newFinished[0].toString()).param("leagueId", leagueId.toString());

				taskQueue.add(taskOptions);
			}
		} else {
			List<Bundle> bundles = bundleDao.getAll();
			Set<String> allInstalledBundlesName = new HashSet<String>();
			for (Bundle b : bundles)
				allInstalledBundlesName.add(b.getName());
			ResourceIterator<String> ri = resourceIteratorProvider.getIterator(DATA_INDEX_PATH);
			for (String s : ri) {
				String[] data = s.split("\\|");
				String buName = data[0];
				Long leId = Converter.toLong(data[1]);
				if (!allInstalledBundlesName.contains(buName)) {
					TaskOptions taskOptions = TaskOptions.Builder.url("/demo/install-questions").param("bundleName", buName).param(
							"finished", "0").param("leagueId", leId.toString());

					taskQueue.add(taskOptions);
					break;
				}
			}
			ri.close();
		}
	}

	public final static long QUESTION_INSTALLATION_BATCH_SIZE = 20;

	private boolean installPackage(String bundleName, Long finished, Long[] newFinished, long maxDuration, League league)
			throws IOException {
		ResourceIterator<String> ri = resourceIteratorProvider.getIterator(bundleName);
		long passed = 0;
		if (finished != null)
			while (passed < finished && ri.hasNext()) {
				ri.next();
				passed++;
			}
		boolean done = false;
		long startTime = timeProvider.getCurrentTimeStamp();
		newFinished[0] = passed;
		while (timeProvider.getCurrentTimeStamp() - startTime < maxDuration) {
			int i = 0;
			List<Question> qs = new ArrayList<Question>();
			while (i < QUESTION_INSTALLATION_BATCH_SIZE && ri.hasNext()) {
				String s = ri.next();
				String[] data = s.split("\\|");
				String question = data[0];
				String[] choices = Arrays.copyOfRange(data, 1, data.length);
				qs.add(createQuestion(league, question, choices));
				i++;
			}
			questionDao.persist(qs.toArray(new Question[qs.size()]));
			newFinished[0] += qs.size();
			if (!ri.hasNext()) {// end of stream
				done = true;
				break;
			}
		}
		ri.close();
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

	static final int WIPE_OUT_BATCH_SIZE = 50;

	private void queueWipeoutJob() {
		TaskOptions taskOptions = TaskOptions.Builder.url("/demo/wipe-out");
		Queue q = QueueFactory.getDefaultQueue();
		q.add(taskOptions);
	}

	@RequestMapping("/wipe-out")
	public void wipeoutData(HttpServletResponse response) {
		if (matchDao.countAll() > 0) {
			List<Match> qs = matchDao.getSubSet(0, WIPE_OUT_BATCH_SIZE);
			for (Match q : qs)
				matchDao.delete(q);
			if (qs.size() == WIPE_OUT_BATCH_SIZE)
				queueWipeoutJob();

		} else if (questionDao.countAll() > 0) {
			List<Question> qs = questionDao.getSubSet(0, WIPE_OUT_BATCH_SIZE);
			for (Question q : qs)
				questionDao.delete(q);
			if (qs.size() == WIPE_OUT_BATCH_SIZE)
				queueWipeoutJob();
		}
	}
}
