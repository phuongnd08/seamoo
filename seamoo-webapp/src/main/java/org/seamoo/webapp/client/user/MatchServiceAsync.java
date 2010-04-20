package org.seamoo.webapp.client.user;

import org.seamoo.entities.matching.MatchState;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MatchServiceAsync {
	public void getMatchState(int bufferedQuestionsCount, int bufferedEventsCount, AsyncCallback<MatchState> callback);

	public void submitAnswer(int questionOrder, String answer, AsyncCallback callback);

	public void ignoreQuestion(int questionOrder, AsyncCallback callback);
}
