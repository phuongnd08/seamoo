package org.seamoo.webapp.client;

import org.seamoo.entities.matching.MatchState;

import com.google.gwt.user.client.rpc.RemoteService;

public interface MatchService extends RemoteService {
	MatchState getMatchState(int bufferedQuestionsCount);
	void submitAnswer(int questionOrder, String answer);
	void ignoreQuestion(int questionOrder);
}
