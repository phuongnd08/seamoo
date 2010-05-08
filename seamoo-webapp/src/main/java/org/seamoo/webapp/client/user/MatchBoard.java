package org.seamoo.webapp.client.user;

import java.util.ArrayList;
import java.util.List;

import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchEvent;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.matching.MatchState;
import org.seamoo.entities.question.Question;
import org.seamoo.webapp.client.user.MatchBoard.Display.EventListener;
import org.seamoo.webapp.client.user.ui.MatchView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class MatchBoard {
	public static interface Display {
		public static interface EventListener {
			public void submitAnswer(Display display, String answer);

			public void ignoreQuestion(Display display);
		}

		public void setPhase(MatchPhase phase);

		public void setCompetitors(List<MatchCompetitor> competitors);

		public void setQuestion(Question question);

		public void addEvents(List<MatchEvent> events);

		public void setTotalQuestion(int total);

		public void setQuestionIndex(int index);

		public void setRemainingTime(long seconds);

		public void addEventListener(EventListener listener);

	}

	public static class Presenter {

		MatchServiceAsync service;
		Display display;
		MatchState currentMatchState;
		boolean waitingForNextQuestion = false;
		Long leagueAutoId;
		EventListener listener = new EventListener() {

			@Override
			public void submitAnswer(Display display, String answer) {
				// Server use 1-based order, but currentQuestionOrder is 0-based
				trySubmitAnswer(currentQuestionOrder + 1, answer, 0/*
																	 * no failed
																	 * yet
																	 */);
				nextQuestion();
			}

			@Override
			public void ignoreQuestion(Display display) {
				// Server use 1-based order, but currentQuestionOrder is 0-based
				tryIgnoreQuestion(currentQuestionOrder + 1, 0);
				nextQuestion();
			}
		};

		public void onModuleLoad(Dictionary dictionary) {
			// TODO Auto-generated method stub
			service = GWT.create(MatchService.class);
			display = new MatchView();
			RootPanel.get("matching-panel").add((Widget) display);
			Long lid = Long.parseLong(dictionary.get("leagueAutoId"));
			initialize(service, display, lid);
		}

		Timer refreshTimer = new Timer() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshMatchState();
			}

		};

		public void rescheduleRefreshTimer() {
			long interval = currentMatchState.getRefreshPeriod();
			if (currentMatchState.getRemainingPeriod() != 0) {
				interval = Math.min(interval, currentMatchState.getRemainingPeriod());
			}
			refreshTimer.schedule((int) interval);
		}

		private long remainingPeriod = 0;
		Timer countdownTimer = new Timer() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setRemainingPeriod(remainingPeriod - 1000);
			}

		};

		private void setRemainingPeriod(long remainingPeriod) {
			countdownTimer.cancel();
			this.remainingPeriod = remainingPeriod > 0 ? remainingPeriod : 0;
			if (this.remainingPeriod > 0)
				countdownTimer.schedule((int) Math.min(1000, this.remainingPeriod));
			display.setRemainingTime(this.remainingPeriod / 1000);
		}

		List<Question> bufferedQuestions;
		List<MatchEvent> bufferedEvents;

		public void initialize(MatchServiceAsync service, final Display display, Long leagueAutoId) {
			// TODO Auto-generated method stub
			this.service = service;
			this.display = display;
			this.leagueAutoId = leagueAutoId;
			display.addEventListener(listener);
			bufferedQuestions = new ArrayList<Question>();
			bufferedEvents = new ArrayList<MatchEvent>();
			refreshMatchState();
		}

		private boolean refreshingMatchState = false;

		public void refreshMatchState() {
			if (refreshingMatchState)
				return;
			refreshingMatchState = true;
			service.getMatchState(leagueAutoId, bufferedQuestions.size(), bufferedEvents.size(), new AsyncCallback<MatchState>() {

				@Override
				public void onSuccess(MatchState state) {
					// TODO Auto-generated method stub
					refreshingMatchState = false;
					setCurrentMatchState(state);
				}

				@Override
				public void onFailure(Throwable throwable) {
					// TODO Auto-generated method stub
					refreshingMatchState = false;
					refreshMatchState();// do not accept failure, retry!
				}
			});

		}

		int currentQuestionOrder = -1;

		private void setCurrentQuestionOrder(int order) {
			currentQuestionOrder = order;
			display.setQuestion(bufferedQuestions.get(order));
			display.setTotalQuestion(currentMatchState.getQuestionsCount());
			display.setQuestionIndex(order + 1);
		}

		private void showNoQuestion() {
			display.setQuestion(null);
		}

		private void setCurrentMatchState(MatchState state) {
			// TODO Auto-generated method stub
			currentMatchState = state;
			display.setPhase(state.getPhase());
			if (state.getPhase() == MatchPhase.FINISHED) {
				Window.Location.replace("/matches/" + state.getMatchAutoId() + "/" + state.getMatchAlias());
				return;
			}
			if (state.getBufferedQuestions() != null) {
				List<Question> qs = state.getBufferedQuestions();
				int from = state.getBufferedFrom();
				if (from > bufferedQuestions.size())
					throw new RuntimeException("Miss-matched bufferedFrom and current buffer size");
				for (int i = bufferedQuestions.size(); i < from; i++) {
					bufferedQuestions.add(null);// add null question to
					// the buffered
					// questions so that the
					// index become
					// consistent
				}
				for (int i = 0; i < qs.size(); i++) {
					assert bufferedQuestions.size() == from + i : "Given bufferedFrom is miss-matched with expected index";
					bufferedQuestions.add(qs.get(i));
				}
			}
			if (state.getBufferedEvents() != null) {
				bufferedEvents.addAll(state.getBufferedEvents());
				display.addEvents(state.getBufferedEvents());
			}
			if (state.getPhase() == MatchPhase.PLAYING) {
				if (currentQuestionOrder == -1) {
					setCurrentQuestionOrder(state.getCompletedQuestionsCount());
				} else if (waitingForNextQuestion)
					nextQuestion();
			}

			display.setCompetitors(state.getCompetitors());
			this.setRemainingPeriod(state.getRemainingPeriod());
			rescheduleRefreshTimer();

		}

		private int MAX_TRY_TIMES = 3;

		private void trySubmitAnswer(final int questionOrder, final String answer, final int triedTimes) {
			service.submitAnswer(leagueAutoId, questionOrder, answer, new AsyncCallback() {

				@Override
				public void onFailure(Throwable throwable) {
					// TODO Auto-generated method stub
					if (triedTimes < MAX_TRY_TIMES) {
						trySubmitAnswer(questionOrder, answer, triedTimes + 1);
					} else
						throw new RuntimeException("Tried " + MAX_TRY_TIMES + " times but cannot submit the answer");
				}

				@Override
				public void onSuccess(Object arg0) {
					// TODO Auto-generated method stub

				}
			});
		}

		private void tryIgnoreQuestion(final int questionOrder, final int triedTimes) {
			service.ignoreQuestion(leagueAutoId, questionOrder, new AsyncCallback() {

				@Override
				public void onFailure(Throwable throwable) {
					// TODO Auto-generated method stub
					if (triedTimes < MAX_TRY_TIMES) {
						tryIgnoreQuestion(questionOrder, triedTimes + 1);
					} else
						throw new RuntimeException("Tried " + MAX_TRY_TIMES + " times but cannot ignore the question");
				}

				@Override
				public void onSuccess(Object arg0) {
					// TODO Auto-generated method stub

				}
			});
		}

		private void nextQuestion() {
			if (currentQuestionOrder < bufferedQuestions.size() - 1) {
				waitingForNextQuestion = false;
				this.setCurrentQuestionOrder(currentQuestionOrder + 1);
				if (currentQuestionOrder == bufferedQuestions.size() - 2
						&& bufferedQuestions.size() < currentMatchState.getQuestionsCount()) {
					// short circuit the refreshTimer
					refreshTimer.cancel();
					refreshTimer.run();
				}
			} else {
				if (bufferedQuestions.size() < currentMatchState.getQuestionsCount()) {
					// questions is being requested
					waitingForNextQuestion = true;
					showNoQuestion();
				} else {
					// no more questions, user has finished his match
					showNoQuestion();
					refreshTimer.cancel();
					refreshTimer.run();
				}
			}
		}
	}

}