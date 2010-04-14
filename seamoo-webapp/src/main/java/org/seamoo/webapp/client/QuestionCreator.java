package org.seamoo.webapp.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seamoo.entities.League;
import org.seamoo.entities.Subject;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionType;
import org.seamoo.webapp.client.QuestionCreator.Display.EventListener;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class QuestionCreator {
	public static interface Display {
		public static interface EventListener {
			public void selectSubject(Display display, Subject subject);

			public void selectLeague(Display display, League league);

			public void selectQuestionType(Display display, QuestionType type);

			public void saveQuestion(Display display, Question question);

		}

		public void setSubjects(List<Subject> subjects);

		public void setLeagues(List<League> leagues);

		public void setQuestionType(QuestionType type);

		public void addEventListener(EventListener listener);

		public Subject getCurrentSubject();

		public League getCurrentLeague();

		public void setErrorMessage(String errorMessage);

	}

	public static class Presenter implements EntryPoint {

		@Override
		public void onModuleLoad() {
			// TODO Auto-generated method stub
		}

		UserQuestionServiceAsync service;
		Display display;
		EventListener listener = new EventListener() {

			@Override
			public void selectSubject(Display display, Subject subject) {
				// TODO Auto-generated method stub
				loadLeagues(subject);
			}

			@Override
			public void selectLeague(Display display, League league) {
				// TODO Auto-generated method stub

			}

			@Override
			public void selectQuestionType(Display display, QuestionType type) {
				// TODO Auto-generated method stub
				display.setQuestionType(type);
			}

			@Override
			public void saveQuestion(final Display display, Question question) {
				// TODO Auto-generated method stub
				service.create(display.getCurrentLeague().getAutoId(), question, new AsyncCallback<Question>() {

					@Override
					public void onFailure(Throwable throwable) {
						// TODO Auto-generated method stub
						display.setErrorMessage(throwable.getMessage());
					}

					@Override
					public void onSuccess(Question question) {
						// TODO Auto-generated method stub
						display.setErrorMessage("Question created");
						Window.Location.replace("/questions/" + question.getAutoId() + "/" + question.getAlias());
					}
				});

			}

		};

		public void initialize(final Display display) {
			service = GWT.create(UserQuestionService.class);
			this.display = display;
			display.addEventListener(listener);
			service.loadAllEnabledSubjects(new AsyncCallback<List<Subject>>() {

				@Override
				public void onSuccess(List<Subject> subjects) {
					// TODO Auto-generated method stub
					display.setSubjects(subjects);
					loadLeagues(subjects.get(0));
				}

				@Override
				public void onFailure(Throwable throwable) {
					// TODO Auto-generated method stub
					throw new RuntimeException(throwable);
				}
			});
		}

		Map<Long, List<League>> cachedLeagues = new HashMap<Long, List<League>>();

		public void loadLeagues(final Subject subject) {
			if (cachedLeagues.containsKey(subject.getAutoId())) {
				display.setLeagues(cachedLeagues.get(subject.getAutoId()));
			} else
				service.loadAllEnabledLeagues(subject.getAutoId(), new AsyncCallback<List<League>>() {

					@Override
					public void onSuccess(List<League> leagues) {
						// TODO Auto-generated method stub
						cachedLeagues.put(subject.getAutoId(), leagues);
						display.setLeagues(leagues);
					}

					@Override
					public void onFailure(Throwable throwable) {
						// TODO Auto-generated method stub
						throw new RuntimeException(throwable);
					}
				});
		}

	}
}