package org.seamoo.webapp.client.admin;

import java.util.List;

import org.seamoo.entities.Subject;
import org.workingonit.gwtbridge.GwtRemoteService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("../services/subject")
@GwtRemoteService("subject")
public interface SubjectService extends RemoteService {
	Subject save(Subject subject);

	List<Subject> getAll();

	void delete(Subject subject);
	
	Subject load(Integer id);
}
