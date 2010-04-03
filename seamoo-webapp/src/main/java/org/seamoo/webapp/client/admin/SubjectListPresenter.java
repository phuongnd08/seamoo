package org.seamoo.webapp.client.admin;

import java.util.List;
import java.util.ArrayList;

import org.seamoo.entities.Subject;
import org.seamoo.webapp.client.admin.SubjectListPresenter.SubjectDisplay.SubjectDisplayEventListener;
import org.seamoo.webapp.client.admin.SubjectListPresenter.SubjectDisplay.SubjectDisplayMode;
import org.seamoo.webapp.client.admin.ui.SubjectListView;
import org.seamoo.webapp.client.admin.ui.SubjectView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class SubjectListPresenter implements EntryPoint {
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
		}

		Subject getSubject();

		void setSubject(Subject subject);

		void setMode(SubjectDisplayMode mode);

		void addEventListener(SubjectDisplayEventListener listener);

	}

	public interface SubjectListDisplay {
		void addSubjectDisplay(SubjectDisplay subjectDisplay);

		void removeSubjectDisplay(SubjectDisplay subjectDisplay);

		SubjectDisplay createSubjectDisplay();
	}

	private ArrayList<SubjectView> subjectBoxes;

	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		// remove Loading-Message from page
		DOM.getElementById("loading-message").removeFromParent();
		RootPanel rootPanel = RootPanel.get("subject-list");
		SubjectListDisplay view = new SubjectListView();
		SubjectServiceAsync subjectServiceAsync = SubjectServiceAsync.Util
				.getInstance();
		rootPanel.add((Widget) view);
		initialize(view, subjectServiceAsync);
	}

	private SubjectDisplayEventListener listener;
	private SubjectServiceAsync subjectServiceAsync;
	private SubjectListDisplay subjectListDisplay;

	public void initialize(final SubjectListDisplay subjectListDisplay,
			final SubjectServiceAsync subjectServiceAsync) {

		this.subjectListDisplay = subjectListDisplay;
		this.subjectServiceAsync = subjectServiceAsync;
		final SubjectListPresenter presenter = this;// closure trick: for
		// presenter.listener to be
		// available inside a method
		// of presenter.listener
		subjectServiceAsync.getAll(new AsyncCallback<List>() {

			@Override
			public void onSuccess(List subjects) {
				presenter.listener = new SubjectDisplayEventListener() {

					@Override
					public void update(SubjectDisplay display, Subject subject) {
						// TODO Auto-generated method stub

					}

					@Override
					public void edit(SubjectDisplay display) {
						// TODO Auto-generated method stub
						display.setMode(SubjectDisplayMode.EDIT);
					}

					@Override
					public void delete(final SubjectDisplay display,
							Subject subject) {
						// TODO Auto-generated method stub
						if (Window.confirm(String.format("Delete subject %s",
								subject.getName()))) {
							subjectServiceAsync.delete(subject,
									new AsyncCallback<Void>() {

										@Override
										public void onSuccess(Void arg0) {
											// TODO Auto-generated method stub
											subjectListDisplay
													.removeSubjectDisplay(display);
										}

										@Override
										public void onFailure(
												Throwable throwable) {
											// TODO Auto-generated method stub
											Window.alert(throwable.toString());
										}
									});
						}
					}

					@Override
					public void create(final SubjectDisplay display,
							Subject subject) {
						// TODO Auto-generated method stub
						// subjectServiceAsync.persist
						subjectServiceAsync.persist(subject,
								new AsyncCallback<Object>() {

									@Override
									public void onSuccess(Object arg0) {
										// TODO Auto-generated method stub
										display.setSubject((Subject) arg0);
										display
												.setMode(SubjectDisplayMode.VIEW);
										// create new SubjectDisplay
										SubjectDisplay newDisplay = subjectListDisplay
												.createSubjectDisplay();
										newDisplay.setSubject(new Subject());
										newDisplay
												.addEventListener(presenter.listener);
										newDisplay
												.setMode(SubjectDisplayMode.CREATE);
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
				};

				// TODO Auto-generated method stub
				for (int i = 0; i < subjects.size(); i++) {
					SubjectDisplay subjectDisplay = subjectListDisplay
							.createSubjectDisplay();
					subjectDisplay.setSubject((Subject) subjects.get(i));
					subjectDisplay.addEventListener(listener);
					subjectDisplay.setMode(SubjectDisplayMode.VIEW);
					subjectListDisplay.addSubjectDisplay(subjectDisplay);
				}
				SubjectDisplay newSubjectDisplay = subjectListDisplay
						.createSubjectDisplay();
				newSubjectDisplay.setSubject(new Subject());
				newSubjectDisplay.addEventListener(listener);
				newSubjectDisplay.setMode(SubjectDisplayMode.CREATE);
				subjectListDisplay.addSubjectDisplay(newSubjectDisplay);
			}

			@Override
			public void onFailure(Throwable throwable) {
				// TODO Auto-generated method stub
				throw new RuntimeException(throwable);
			}
		});
	}
}
