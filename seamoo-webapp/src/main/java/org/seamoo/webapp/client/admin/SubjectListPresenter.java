package org.seamoo.webapp.client.admin;

import java.util.List;
import java.util.ArrayList;

import org.seamoo.entities.Subject;
import org.seamoo.webapp.client.admin.SubjectListPresenter.SubjectDisplay.SubjectDisplayEventListener;
import org.seamoo.webapp.client.admin.SubjectListPresenter.SubjectDisplay.SubjectDisplayMode;
import org.seamoo.webapp.client.admin.ui.SubjectListView;
import org.seamoo.webapp.client.admin.ui.SubjectView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class SubjectListPresenter {
	public interface SubjectDisplay {
		public enum SubjectDisplayMode {
			VIEW, CREATE, EDIT
		}

		public interface SubjectDisplayEventListener {
			void edit(SubjectDisplay display);

			void cancelEdit(SubjectDisplay display);

			void update(SubjectDisplay display, Subject subject);

			void create(SubjectDisplay display, Subject subject);

			void delete(SubjectDisplay display, Subject subject);

			void selectLogo(SubjectDisplay display, String logoUrl);
		}

		Subject getSubject();

		void setSubject(Subject subject);

		void setMode(SubjectDisplayMode mode);

		void setLogoUrl(String url);

		String getLogoUrl();

		void addEventListener(SubjectDisplayEventListener listener);

	}

	public interface SubjectListDisplay {
		void addSubjectDisplay(SubjectDisplay subjectDisplay);

		void removeSubjectDisplay(SubjectDisplay subjectDisplay);

		SubjectDisplay createSubjectDisplay();
	}

	private ArrayList<SubjectView> subjectBoxes;

	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// remove Loading-Message from page
		RootPanel rootPanel = RootPanel.get("subject-list");
		SubjectListDisplay view = new SubjectListView();
		SubjectServiceAsync subjectServiceAsync = GWT.create(SubjectService.class);
		rootPanel.add((Widget) view);
		initialize(view, subjectServiceAsync, new AsyncCallback() {

			@Override
			public void onFailure(Throwable throwable) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(Object arg0) {
				// TODO Auto-generated method stub
				DOM.getElementById("loading-message").removeFromParent();
			}
		});
	}

	private SubjectDisplayEventListener listener;
	private SubjectServiceAsync subjectServiceAsync;
	private SubjectListDisplay subjectListDisplay;

	public void initialize(final SubjectListDisplay subjectListDisplay, final SubjectServiceAsync subjectServiceAsync,
			final AsyncCallback subjectLoadedCallback) {

		this.subjectListDisplay = subjectListDisplay;
		this.subjectServiceAsync = subjectServiceAsync;
		final SubjectListPresenter presenter = this;// closure trick: for
		// presenter.listener to be
		// available inside a method
		// of presenter.listener
		subjectServiceAsync.getAll(new AsyncCallback<List<Subject>>() {

			@Override
			public void onSuccess(List<Subject> subjects) {
				presenter.listener = new SubjectDisplayEventListener() {

					@Override
					public void update(final SubjectDisplay display, Subject subject) {
						// TODO Auto-generated method stub
						subjectServiceAsync.save(subject, new AsyncCallback<Subject>() {

							@Override
							public void onSuccess(Subject arg0) {
								// TODO Auto-generated method stub
								display.setSubject(arg0);
								display.setMode(SubjectDisplayMode.VIEW);
							}

							@Override
							public void onFailure(Throwable throwable) {
								// TODO Auto-generated method stub

							}
						});
					}

					@Override
					public void edit(SubjectDisplay display) {
						// TODO Auto-generated method stub
						display.setMode(SubjectDisplayMode.EDIT);
					}

					@Override
					public void delete(final SubjectDisplay display, Subject subject) {
						// TODO Auto-generated method stub
						if (Window.confirm("Delete subject " + subject.getName() + "?")) {
							subjectServiceAsync.delete(subject, new AsyncCallback<Void>() {

								@Override
								public void onSuccess(Void arg0) {
									// TODO Auto-generated method stub
									subjectListDisplay.removeSubjectDisplay(display);
								}

								@Override
								public void onFailure(Throwable throwable) {
									// TODO Auto-generated method stub
									Window.alert(throwable.toString());
								}
							});
						}
					}

					@Override
					public void create(final SubjectDisplay display, Subject subject) {
						// TODO Auto-generated method stub
						// subjectServiceAsync.persist
						subjectServiceAsync.save(subject, new AsyncCallback<Subject>() {

							@Override
							public void onSuccess(Subject subject) {
								// TODO Auto-generated method stub
								display.setSubject((Subject) subject);
								display.setMode(SubjectDisplayMode.VIEW);
								// create new SubjectDisplay
								SubjectDisplay newDisplay = subjectListDisplay.createSubjectDisplay();
								newDisplay.setSubject(new Subject());
								newDisplay.addEventListener(presenter.listener);
								newDisplay.setMode(SubjectDisplayMode.CREATE);
								subjectListDisplay.addSubjectDisplay(newDisplay);
							}

							@Override
							public void onFailure(Throwable throwable) {
								// TODO Auto-generated method stub
								Window.alert(throwable.toString());
							}
						});

					}

					@Override
					public void cancelEdit(SubjectDisplay display) {
						// TODO Auto-generated method stub
						display.setMode(SubjectDisplayMode.VIEW);

					}

					@Override
					public void selectLogo(SubjectDisplay display, String logoUrl) {
						// TODO Auto-generated method stub
						String newLogoUrl = Window.prompt("Input the new logo image url", logoUrl);
						if (newLogoUrl != null) {
							display.setLogoUrl(newLogoUrl);
						}
					}
				};

				// TODO Auto-generated method stub
				for (int i = 0; i < subjects.size(); i++) {
					SubjectDisplay subjectDisplay = subjectListDisplay.createSubjectDisplay();
					subjectDisplay.setSubject(subjects.get(i));
					subjectDisplay.addEventListener(listener);
					subjectDisplay.setMode(SubjectDisplayMode.VIEW);
					subjectListDisplay.addSubjectDisplay(subjectDisplay);
				}
				SubjectDisplay newSubjectDisplay = subjectListDisplay.createSubjectDisplay();
				newSubjectDisplay.setSubject(new Subject());
				newSubjectDisplay.addEventListener(listener);
				newSubjectDisplay.setMode(SubjectDisplayMode.CREATE);
				subjectListDisplay.addSubjectDisplay(newSubjectDisplay);
				if (subjectLoadedCallback != null)
					subjectLoadedCallback.onSuccess(null);
			}

			@Override
			public void onFailure(Throwable throwable) {
				// TODO Auto-generated method stub
				throw new RuntimeException(throwable);
			}
		});
	}
}
