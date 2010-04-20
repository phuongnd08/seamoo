package org.seamoo.entities.matching;

import java.io.Serializable;
import java.util.List;

import org.seamoo.entities.question.Question;

/**
 * Contain all information used to re-produce the match state of a player. Won't
 * be persisted. Will be calculated based on Match & MatchCompetitor information
 * 
 * @author phuongnd08
 * 
 */
public class MatchState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8236758819315756620L;
	public static final int DEFAULT_REFRESH_PERIOD = 1000;// milliseconds
	private long matchAutoId;
	/**
	 * The total number of questions used in match
	 */
	private int questionsCount;

	/**
	 * The number of periods after that the player should recheck for match
	 * state
	 */
	private int refreshPeriod = DEFAULT_REFRESH_PERIOD;
	/**
	 * The number of periods after that the current phase will end If the
	 * current phase is FORMED then this is the number of period before the
	 * match begin If the current phase is PLAYING then this is the number of
	 * period before the match end
	 */
	private int remainingPeriod;
	/**
	 * The zero-based index from which questions is buffered for current player
	 */
	private int bufferedQuestionsFrom;
	private List<Question> bufferedQuestions;
	private int bufferedEventsFrom;
	private List<MatchEvent> bufferedEvents;
	private List<MatchCompetitor> competitors;
	private MatchPhase phase;
	private int completedQuestionsCount;

	public void setMatchAutoId(long matchAutoId) {
		this.matchAutoId = matchAutoId;
	}

	public long getMatchAutoId() {
		return matchAutoId;
	}

	public void setBufferedQuestionsFrom(int bufferedFrom) {
		this.bufferedQuestionsFrom = bufferedFrom;
	}

	public int getBufferedFrom() {
		return bufferedQuestionsFrom;
	}

	public void setBufferedQuestions(List<Question> bufferedQuestions) {
		this.bufferedQuestions = bufferedQuestions;
	}

	public List<Question> getBufferedQuestions() {
		return bufferedQuestions;
	}

	public void setPhase(MatchPhase phase) {
		this.phase = phase;
	}

	public MatchPhase getPhase() {
		return phase;
	}

	public void setQuestionsCount(int questionsCount) {
		this.questionsCount = questionsCount;
	}

	public int getQuestionsCount() {
		return questionsCount;
	}

	public void setRefreshPeriod(int refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}

	public int getRefreshPeriod() {
		return refreshPeriod;
	}

	public void setRemainingPeriod(int remainingPeriod) {
		this.remainingPeriod = remainingPeriod;
	}

	public int getRemainingPeriod() {
		return remainingPeriod;
	}

	public void setCompetitors(List<MatchCompetitor> competitors) {
		this.competitors = competitors;
	}

	public List<MatchCompetitor> getCompetitors() {
		return competitors;
	}

	public void setCompletedQuestionsCount(int completedQuestionsCount) {
		this.completedQuestionsCount = completedQuestionsCount;
	}

	public int getCompletedQuestionsCount() {
		return completedQuestionsCount;
	}

	public void setBufferedEventsFrom(int bufferedEventsFrom) {
		this.bufferedEventsFrom = bufferedEventsFrom;
	}

	public int getBufferedEventsFrom() {
		return bufferedEventsFrom;
	}

	public void setBufferedEvents(List<MatchEvent> bufferedEvents) {
		this.bufferedEvents = bufferedEvents;
	}

	public List<MatchEvent> getBufferedEvents() {
		return bufferedEvents;
	}
}
