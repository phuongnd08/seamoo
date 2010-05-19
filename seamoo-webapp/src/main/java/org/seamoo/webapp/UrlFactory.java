package org.seamoo.webapp;

import java.net.URLEncoder;

import org.seamoo.entities.League;
import org.seamoo.entities.Member;
import org.seamoo.entities.Subject;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchState;

public class UrlFactory {
	public final static String getAvatarUrl(String emailHash, int size) {
		return "http://www.gravatar.com/avatar/" + emailHash + "?s=" + size + "&d=wavatar&r=PG";
	}

	public final static String getUserViewUrl(Member user) {
		return "/users/" + user.getAutoId() + "/" + user.getAlias();
	}

	public final static String getMatchViewUrl(Match match) {
		return "/matches/" + match.getAutoId() + "/" + match.getAlias();
	}

	public final static String getRejoinableMatchViewUrl(MatchState state) {
		return "/matches/" + state.getMatchAutoId() + "/" + state.getMatchAlias() + "?rejoin=yes";
	}

	public static String getLeagueViewUrl(League league) {
		return "/leagues/" + league.getAutoId() + "/" + league.getAlias();
	}

	public static String getSubjectViewUrl(Subject subject) {
		return "/subjects/" + subject.getAutoId() + "/" + subject.getAlias();
	}

	public static String getParticipateUrl(League league) {
		return "/matches/participate?leagueId=" + league.getAutoId();
	}

	public static String getLogInUrl(String returnUrl) {
		return "/users/login?returnUrl=" + URLEncoder.encode(returnUrl);
	}

	public static String getPagedLeagueViewUrl(League league, long rankPage, long matchPage) {
		return getLeagueViewUrl(league) + "?r=" + rankPage + "&m=" + matchPage;
	}

	public static String getPagedUserViewUrl(Member member, long matchPage, long membershipPage) {
		return getUserViewUrl(member) + "?m=" + matchPage + "&ms=" + membershipPage;
	}

}
