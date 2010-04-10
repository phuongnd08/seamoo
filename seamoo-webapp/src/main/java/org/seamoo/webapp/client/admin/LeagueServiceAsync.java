package org.seamoo.webapp.client.admin;

import java.util.List;

import org.seamoo.entities.League;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LeagueServiceAsync {

	void delete(League league, AsyncCallback<Void> callback);

	void getAll(Long subjectId, AsyncCallback<List<League>> callback);

	void save(Long subjectId, League League, AsyncCallback<League> callback);
}
