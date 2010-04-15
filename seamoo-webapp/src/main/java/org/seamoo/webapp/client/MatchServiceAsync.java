package org.seamoo.webapp.client;

import org.seamoo.entities.matching.MatchState;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MatchServiceAsync {
	public void getMatchState(int bufferedQuestionsCount, AsyncCallback<MatchState> callback);

	public void submitAnswer(int questionOrder, String answer, AsyncCallback callback);

	public void ignoreQuestion(int questionOrder, AsyncCallback callback);
}
