package org.seamoo.webapp.controllers;

import java.util.List;

import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.lookup.NumericBagDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.daos.speed.QuestionEventDao;
import org.seamoo.entities.League;
import org.seamoo.entities.question.Question;
import org.seamoo.lookup.NumericBag;
import org.seamoo.speed.QuestionEvent;
import org.seamoo.utils.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;

@RequestMapping("/maintain")
public class MaintainerController {
	@Autowired
	NumericBagDao numericBagDao;

	@Autowired
	QuestionDao questionDao;

	@Autowired
	LeagueDao leagueDao;

	@Autowired
	TimeProvider timeProvider;

	@Autowired
	QuestionEventDao questionEventDao;

	public final static long MAX_QUESTION_INDEX_REFRESH_PERIOD = 20000;
	public final static int QUESTION_INDEX_BATCH_SIZE = 50;

	@RequestMapping("/refresh-question-indexes")
	public void refreshQuestionIndexes() {
		internalRefreshQuestionIndexes(QueueFactory.getDefaultQueue());
	}

	void internalRefreshQuestionIndexes(Queue taskQueue) {
		List<League> leagues = leagueDao.getAll();
		for (League l : leagues) {
			TaskOptions t = TaskOptions.Builder.url("/maintain/refresh-league-question-index").param("leagueId",
					l.getAutoId().toString());
			taskQueue.add(t);
		}
	}

	@RequestMapping("/refresh-league-question-index")
	public void refreshLeagueQuestionIndexes(@RequestParam("leagueId") Long leagueId) {
		long startTime = timeProvider.getCurrentTimeStamp();
		String classifier = NumericBag.getUniformClassifier(Question.class, leagueId);
		NumericBag numericBag = numericBagDao.findByClassifier(classifier);
		if (numericBag == null) {
			numericBag = new NumericBag();
			numericBag.setClassifier(NumericBag.getUniformClassifier(Question.class, leagueId));
		}
		do {
			List<QuestionEvent> events = questionEventDao.getAllByMinimumTimeStamp(leagueId,
					numericBag.getLastUpdatedTimestamp(), QUESTION_INDEX_BATCH_SIZE);
			for (QuestionEvent event : events) {
				switch (event.getType()) {
				case CREATE:
					numericBag.addNumber(event.getQuestionAutoId());
					break;
				case DELETE:
					numericBag.removeNumber(event.getQuestionAutoId());
					break;
				}
				numericBag.setLastUpdatedTimestamp(Math.max(numericBag.getLastUpdatedTimestamp(), event.getTimeStamp()));
			}
			if (events.size() < QUESTION_INDEX_BATCH_SIZE)
				break;
		} while (startTime + MAX_QUESTION_INDEX_REFRESH_PERIOD > timeProvider.getCurrentTimeStamp());
		numericBagDao.persist(numericBag);
	}
}
