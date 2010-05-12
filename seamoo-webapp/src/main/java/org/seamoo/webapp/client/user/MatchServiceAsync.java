package org.seamoo.webapp.client.user;

import org.seamoo.entities.League;
import org.seamoo.entities.matching.MatchState;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MatchServiceAsync {
	public void getMatchState(Long leagueId, int bufferedQuestionsCount, int bufferedEventsCount,
			AsyncCallback<MatchState> callback);

	public void submitAnswer(Long leagueId, int questionOrder, String answer, AsyncCallback callback);

	public void ignoreQuestion(Long leagueId, int questionOrder, AsyncCallback callback);

	public void escapeCurrentMatch(Long leagueId, AsyncCallback<League> asyncCallback);
}
