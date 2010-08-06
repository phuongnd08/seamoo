package org.seamoo.webapp.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.SiteSettingDao;
import org.seamoo.daos.SubjectDao;
import org.seamoo.daos.installation.BundleDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.League;
import org.seamoo.entities.Member;
import org.seamoo.entities.Subject;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchAnswer;
import org.seamoo.entities.matching.MatchAnswerType;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.question.FollowPatternQuestionRevision;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.installation.Bundle;
import org.seamoo.utils.HashBuilder;
import org.seamoo.utils.PatternExtractor;
import org.seamoo.utils.ResourceIterator;
import org.seamoo.utils.ResourceIteratorProvider;
import org.seamoo.utils.TimeProvider;
import org.seamoo.utils.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;

@Controller
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	SiteSettingDao siteSettingDao;

	@Autowired
	SubjectDao subjectDao;

	@Autowired
	LeagueDao leagueDao;

	@Autowired
	MemberDao memberDao;

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
	public ModelAndView installLeagues() throws IOException {
		String msg = "";
		boolean leaguesInstalled = Converter.toBoolean(siteSettingDao.getSetting("leaguesInstalled"));
		if (!leaguesInstalled) {
			msg = "Leagues is being installed<br/>";
			Subject[] subjects = internalInstallSubjects();
			League[] leagues = internalInstallLeagues(subjects[0]);
			siteSettingDao.assignSetting("leaguesInstalled", Converter.toString(true));
			msg += "Install done!<br/>";
		} else {
			msg = "Error: League Installed";
		}
		ModelAndView mav = new ModelAndView("basic.dumb");
		mav.addObject("msg", msg);
		return mav;
	}
	
	@RequestMapping("/showoff")
	public ModelAndView showOff() throws IOException {
		ModelAndView mav = new ModelAndView("basic.dumb");
		mav.addObject("msg", org.seamoo.scala.ScalaPower.showMyPower(100));
		return mav;
	}

	@RequestMapping("/install-samples")
	public ModelAndView installSamples() throws IOException {
		String msg = "";
		boolean samplesInstalled = Converter.toBoolean(siteSettingDao.getSetting("samplesInstalled"));
		if (!samplesInstalled) {
			msg = "Start installing samples<br/>";
			Subject sampleSubject = new Subject("Sample Subject", "/images/subjects/sample.png", "Sample subject for testing",
					true, new Date());
			subjectDao.persist(sampleSubject);
			League sampleLeague = new League("Sample League", "sample-league", "/images/leagues/sample.png",
					"Sample league for testing", 0, true, new Date());
			sampleLeague.setSubjectAutoId(sampleSubject.getAutoId());
			leagueDao.persist(sampleLeague);
			Member[] sampleMembers = new Member[] {
					new Member("http://localhost:3000/user/phuongnd08", "Phuong Nguyen", "phuongnd08",
							HashBuilder.getMD5Hash("phuongnd08@seamoo.com"), false),
					new Member("http://localhost:3000/user/mrcold", "Mr Cold", "mrcold",
							HashBuilder.getMD5Hash("mrcold@seamoo.com"), true),
					new Member("http://localhost:3000/user/stranger", "Stranger", "stranger",
							HashBuilder.getMD5Hash("stranger@seamoo.com"), false) };
			memberDao.persist(sampleMembers);
			installPackage("classpath:sample_questions.data", 0L, new Long[1], MAX_DURATION, sampleLeague);

			// Get a list of first 2 sample questions
			List<Question> questions = questionDao.getSubSet(0, 20);
			List<Long> questionIds = new ArrayList<Long>();
			for (Question q : questions)
				questionIds.add(q.getAutoId());
			Collections.sort(questionIds);
			// Generate 2 sample matches
			for (int i = 0; i < 2; i++) {
				Match sampleMatch = new Match();
				sampleMatch.setLeagueAutoId(sampleLeague.getAutoId());
				sampleMatch.setQuestionIds(questionIds);
				switch (i) {
				case 0:
					sampleMatch.addCompetitor(generateCompetitor(sampleMembers[0]));
					sampleMatch.addCompetitor(generateCompetitor(sampleMembers[1]));
					sampleMatch.setAlias("phuongnd08-mrcold");
					break;
				case 1:
					sampleMatch.addCompetitor(generateCompetitor(sampleMembers[1]));
					sampleMatch.addCompetitor(generateCompetitor(sampleMembers[2]));
					sampleMatch.setAlias("mrcold-stranger");
					break;
				}
				matchDao.persist(sampleMatch);
			}
			siteSettingDao.assignSetting("samplesInstalled", Converter.toString(true));
			msg += "Done!<br/>";

		} else {
			msg += "Error: Sample Installed";
		}

		ModelAndView mav = new ModelAndView("basic.dumb");
		mav.addObject("msg", msg);
		return mav;
	}

	private MatchCompetitor generateCompetitor(Member member) {
		MatchCompetitor competitor = new MatchCompetitor();
		competitor.setMemberAutoId(member.getAutoId());
		for (int i = 0; i < 20; i++)
			if (i % 2 == 0) {
				int answer = ((i / 2) % 2) + 1;
				competitor.addAnswer(new MatchAnswer(MatchAnswerType.SUBMITTED, String.valueOf(answer)));
			} else
				competitor.addAnswer(new MatchAnswer(MatchAnswerType.IGNORED, null));
		return competitor;
	}

	private Subject[] internalInstallSubjects() {
		Subject english = new Subject("English", "/images/subjects/english.png",
				"Thể hiện kĩ năng Anh ngữ của bạn về từ vựng, ngữ pháp và phát âm", true, new Date());
		Subject maths = new Subject("Toán học", "/images/subjects/math.png",
				"Thể hiện sự hiểu biết cùng khả năng suy luận toán học của bạn", true, new Date());
		Subject history = new Subject("Lịch sử", "/images/subjects/history.png", "Thể hiện sự am tường lịch sử của bạn", false,
				new Date());
		Subject[] subjects = new Subject[] { english, maths, history };
		subjectDao.persist(subjects);
		return subjects;
	}

	private League[] internalInstallLeagues(Subject subject) {
		League englishAmateur = new League("Giải nghiệp dư", "giai-nghiep-du", "/images/leagues/eng-amateur.png",
				"Dành cho những người lần đầu tham gia ", 0, true, new Date());
		League englishChick = new League("Giải gà con", "giai-ga-con", "/images/leagues/eng-league-2.png",
				"Dành cho những đấu thủ đã thể hiện được mình ở Giải nghiệp dư ", 1, true, new Date());
		League englishCock = new League("Giải gà chọi", "giai-ga-choi", "/images/leagues/eng-league-1.png",
				"Dành cho những đấu thủ đẳng cấp ", 2, true, new Date());
		League englishEagle = new League("Giải đại bàng", "giai-dai-bang", "/images/leagues/eng-pro-league.png",
				"Dành cho những đấu thủ đẳng cấp cao", 3, true, new Date());
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
	public ModelAndView installQuestions(@RequestParam(required = false, value = "bundleName") String bundleName,
			@RequestParam(required = false, value = "finished") Long finished,
			@RequestParam(required = false, value = "leagueId") Long leagueId) throws IOException {

		internalInstallQuestions(bundleName, finished, leagueId, QueueFactory.getDefaultQueue());
		ModelAndView mav = new ModelAndView("basic.dumb");
		mav.addObject("msg", "Some questions installed.");
		return mav;

	}

	void internalInstallQuestions(String bundleName, Long finished, Long leagueId, Queue taskQueue) throws IOException {
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

	Question createQuestion(League league, String question, String[] choices) {
		Question q = new Question();
		q.setLeagueAutoId(league.getAutoId());
		if (choices.length == 1) {
			FollowPatternQuestionRevision r = new FollowPatternQuestionRevision();
			r.setContent(question);
			String pattern = choices[0];
			if (pattern.startsWith("*"))
				pattern = pattern.substring(1);
			r.setPattern(pattern);
			r.setGuidingPattern(PatternExtractor.getGuidingPattern(pattern));
			q.addAndSetAsCurrentRevision(r);
		} else {
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
		}
		return q;
	}
}
