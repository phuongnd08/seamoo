package org.seamoo.webapp.client.user;

import org.seamoo.entities.matching.MatchState;
import org.workingonit.gwtbridge.GwtRemoteService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../services/user-match")
@GwtRemoteService("user-match")
public interface MatchService extends RemoteService {
	MatchState getMatchState(int bufferedQuestionsCount, int bufferedEventsCount);

	void submitAnswer(int questionOrder, String answer);

	void ignoreQuestion(int questionOrder);
}
