package org.seamoo.webapp.client.user;

import org.seamoo.entities.League;
import org.seamoo.entities.matching.MatchState;
import org.seamoo.webapp.client.shared.ui.NotLoggedInException;
import org.workingonit.gwtbridge.GwtRemoteService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../services/user-match")
@GwtRemoteService("user-match")
public interface MatchService extends RemoteService {
	MatchState getMatchState(Long leagueId, int bufferedQuestionsCount, int bufferedEventsCount) throws NotLoggedInException;

	void submitAnswer(Long leagueId, int questionOrder, String answer);

	void ignoreQuestion(Long leagueId, int questionOrder);
	
	/**
	 * Escape user from his current match and return the information of the league in which he is matching
	 * @param leagueId
	 * @return
	 */
	League escapeCurrentMatch(Long leagueId);
}
