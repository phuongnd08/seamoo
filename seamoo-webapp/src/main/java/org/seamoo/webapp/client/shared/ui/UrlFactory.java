package org.seamoo.webapp.client.shared.ui;

import org.seamoo.entities.League;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.MatchState;

public class UrlFactory {
	public final static String getAvatarUrl(String emailHash, int size) {
		return "http://www.gravatar.com/avatar/" + emailHash + "?s=" + size + "&d=wavatar&r=PG";
	}

	public final static String getUserViewUrl(Member user) {
		return "/users/" + user.getAutoId() + "/" + user.getAlias();
	}

	public final static String getMatchViewUrl(MatchState state) {
		return "/matches/" + state.getMatchAutoId() + "/" + state.getMatchAlias();
	}

	public static String getLeagueViewUrl(League league) {
		// TODO Auto-generated method stub
		return "/leagues/" + league.getAutoId() + "/" + league.getAlias();
	}
}
