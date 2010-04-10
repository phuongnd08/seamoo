package org.seamoo.webapp.client.admin;

import java.util.ArrayList;
import java.util.List;

import org.seamoo.entities.League;
import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueDisplay.LeagueDisplayEventListener;
import org.seamoo.webapp.client.admin.LeagueListPresenter.LeagueDisplay.LeagueDisplayMode;
import org.seamoo.webapp.client.admin.ui.LeagueListView;
import org.seamoo.webapp.client.admin.ui.SubjectView;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class LeagueListPresenter {
	public interface LeagueDisplay {
		public enum LeagueDisplayMode {
			VIEW, CREATE, EDIT
		}

		public interface LeagueDisplayEventListener {
			void edit(LeagueDisplay display);

			void cancelEdit(LeagueDisplay display);

			void update(LeagueDisplay display, League subject);

			void create(LeagueDisplay display, League subject);

			void delete(LeagueDisplay display, League subject);

			void selectLogo(LeagueDisplay display, String logoUrl);
		}

		League getLeague();

		void setLeague(League subject);

		void setMode(LeagueDisplayMode mode);

		void setLogoUrl(String url);

		String getLogoUrl();

		void addEventListener(LeagueDisplayEventListener listener);

	}

	public interface LeagueListDisplay {
		void addLeagueDisplay(LeagueDisplay leagueDisplay);

		void removeLeagueDisplay(LeagueDisplay leagueDisplay);

		LeagueDisplay createLeagueDisplay();
	}

	private ArrayList<SubjectView> subjectBoxes;

	public void onModuleLoad(Dictionary dictionary) {
		// TODO Auto-generated method stub
		// remove Loading-Message from page
		RootPanel rootPanel = RootPanel.get("league-list");
		LeagueListDisplay view = new LeagueListView();
		LeagueServiceAsync leagueServiceAsync = GWT.create(LeagueService.class);
		rootPanel.add((Widget) view);
		long currentSubjectId = Long.parseLong(dictionary.get("currentSubjectAutoId"));
		initialize(currentSubjectId, view, leagueServiceAsync, new AsyncCallback() {

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

	private LeagueDisplayEventListener listener;
	private LeagueServiceAsync leagueServiceAsync;
	private LeagueListDisplay leagueListDisplay;

	public void initialize(final long currentSubjectId, final LeagueListDisplay leagueListDisplay,
			final LeagueServiceAsync leagueServiceAsync, final AsyncCallback leagueLoadedCallback) {

		this.leagueListDisplay = leagueListDisplay;
		this.leagueServiceAsync = leagueServiceAsync;
		final LeagueListPresenter presenter = this;// closure trick: for
		// presenter.listener to be
		// available inside a method
		// of presenter.listener
		leagueServiceAsync.getAll(currentSubjectId, new AsyncCallback<List<League>>() {

			@Override
			public void onSuccess(List<League> leagues) {
				presenter.listener = new LeagueDisplayEventListener() {

					@Override
					public void update(final LeagueDisplay display, League league) {
						// TODO Auto-generated method stub
						leagueServiceAsync.save(currentSubjectId, league, new AsyncCallback<League>() {

							@Override
							public void onSuccess(League arg0) {
								// TODO Auto-generated method stub
								display.setLeague(arg0);
								display.setMode(LeagueDisplayMode.VIEW);
							}

							@Override
							public void onFailure(Throwable throwable) {
								// TODO Auto-generated method stub

							}
						});
					}

					@Override
					public void edit(LeagueDisplay display) {
						// TODO Auto-generated method stub
						display.setMode(LeagueDisplayMode.EDIT);
					}

					@Override
					public void delete(final LeagueDisplay display, League subject) {
						// TODO Auto-generated method stub
						if (Window.confirm("Delete subject " + subject.getName() + "?")) {
							leagueServiceAsync.delete(subject, new AsyncCallback<Void>() {

								@Override
								public void onSuccess(Void arg0) {
									// TODO Auto-generated method stub
									leagueListDisplay.removeLeagueDisplay(display);
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
					public void create(final LeagueDisplay display, League league) {
						// TODO Auto-generated method stub
						// subjectServiceAsync.persist
						leagueServiceAsync.save(currentSubjectId, league, new AsyncCallback<League>() {

							@Override
							public void onSuccess(League league) {
								// TODO Auto-generated method stub
								display.setLeague(league);
								display.setMode(LeagueDisplayMode.VIEW);
								// create new SubjectDisplay
								LeagueDisplay newDisplay = leagueListDisplay.createLeagueDisplay();
								newDisplay.setLeague(new League());
								newDisplay.addEventListener(presenter.listener);
								newDisplay.setMode(LeagueDisplayMode.CREATE);
								leagueListDisplay.addLeagueDisplay(newDisplay);
							}

							@Override
							public void onFailure(Throwable throwable) {
								// TODO Auto-generated method stub
								Window.alert(throwable.toString());
							}
						});

					}

					@Override
					public void cancelEdit(LeagueDisplay display) {
						// TODO Auto-generated method stub
						display.setMode(LeagueDisplayMode.VIEW);

					}

					@Override
					public void selectLogo(LeagueDisplay display, String logoUrl) {
						// TODO Auto-generated method stub
						String newLogoUrl = Window.prompt("Input the new logo image url", logoUrl);
						if (newLogoUrl != null) {
							display.setLogoUrl(newLogoUrl);
						}
					}
				};

				// TODO Auto-generated method stub
				for (int i = 0; i < leagues.size(); i++) {
					LeagueDisplay subjectDisplay = leagueListDisplay.createLeagueDisplay();
					subjectDisplay.setLeague(leagues.get(i));
					subjectDisplay.addEventListener(listener);
					subjectDisplay.setMode(LeagueDisplayMode.VIEW);
					leagueListDisplay.addLeagueDisplay(subjectDisplay);
				}
				LeagueDisplay newSubjectDisplay = leagueListDisplay.createLeagueDisplay();
				newSubjectDisplay.setLeague(new League());
				newSubjectDisplay.addEventListener(listener);
				newSubjectDisplay.setMode(LeagueDisplayMode.CREATE);
				leagueListDisplay.addLeagueDisplay(newSubjectDisplay);
				if (leagueLoadedCallback != null)
					leagueLoadedCallback.onSuccess(null);
			}

			@Override
			public void onFailure(Throwable throwable) {
				// TODO Auto-generated method stub
				throw new RuntimeException(throwable);
			}
		});
	}
}
