package org.seamoo.webapp.client.admin;

import java.util.List;

import org.seamoo.entities.League;
import org.workingonit.gwtbridge.GwtRemoteService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../services/league")
@GwtRemoteService("league")
public interface LeagueService extends RemoteService {
	League save(Long subjectId, League subject);

	List<League> getAll(Long subjectId);

	void delete(League subject);
}
