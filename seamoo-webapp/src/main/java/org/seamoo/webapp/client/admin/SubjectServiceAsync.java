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

	void load(Integer id, AsyncCallback<Subject> callback);

	/**
	 * Utility class to get the RPC Async interface from client-side code
	 */
	public static final class Util {
		private static SubjectServiceAsync instance;

		public static final SubjectServiceAsync getInstance() {
			if (instance == null) {
				instance = (SubjectServiceAsync) GWT.create(SubjectService.class);
				ServiceDefTarget target = (ServiceDefTarget) instance;
				target.setServiceEntryPoint(GWT.getModuleBaseURL() + "SubjectService");
			}
			return instance;
		}

		private Util() {
			// Utility class should not be instanciated
		}
	}
}
