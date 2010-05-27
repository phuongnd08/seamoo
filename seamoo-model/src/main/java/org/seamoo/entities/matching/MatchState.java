package org.seamoo.entities.matching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.seamoo.entities.question.Question;

/**
 * Contain all information used to re-produce the match state of a player. Won't be persisted. Will be calculated based on Match &
 * MatchCompetitor information
 * 
 * @author phuongnd08
 * 
 */
public class MatchState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8236758819315756620L;
	public static final int DEFAULT_REFRESH_PERIOD = 3000;// milliseconds
	private long matchAutoId;
	private long leagueAutoId;
	private String matchAlias;
	/**
	 * The total number of questions used in match
	 */
	private int questionsCount;

	/**
	 * The number of periods after that the player should recheck for match state
	 */
	private long refreshPeriod = DEFAULT_REFRESH_PERIOD;
	/**
	 * The number of periods after that the current phase will end If the current phase is FORMED then this is the number of
	 * period before the match begin If the current phase is PLAYING then this is the number of period before the match end
	 */
	private long remainingPeriod;
	/**
	 * The zero-based index from which questions is buffered for current player
	 */
	private int bufferedQuestionsFrom;
	private List<Question> bufferedQuestions;
	private List<MatchCompetitor> competitors;
	private MatchPhase phase;
	private int completedQuestionsCount;

	public MatchState() {
		bufferedQuestions = new ArrayList<Question>();
	}

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

	public void setRefreshPeriod(long refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
	}

	public long getRefreshPeriod() {
		return refreshPeriod;
	}

	public void setRemainingPeriod(long remainingPeriod) {
		this.remainingPeriod = remainingPeriod;
	}

	public long getRemainingPeriod() {
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

	public void setMatchAlias(String matchAlias) {
		this.matchAlias = matchAlias;
	}

	public String getMatchAlias() {
		return matchAlias;
	}

	public void setLeagueAutoId(long leagueAutoId) {
		this.leagueAutoId = leagueAutoId;
	}

	public long getLeagueAutoId() {
		return leagueAutoId;
	}
}
