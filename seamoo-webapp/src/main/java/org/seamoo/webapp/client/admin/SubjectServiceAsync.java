package org.seamoo.webapp.client.admin;

import java.util.List;

import org.seamoo.entities.Subject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface SubjectServiceAsync {

	void delete(Subject subject, AsyncCallback<Void> callback);

	void getAll(AsyncCallback<List<Subject>> callback);

	void save(Subject subject, AsyncCallback<Subject> callback);

}
