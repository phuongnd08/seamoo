package org.seamoo.webapp.client.user.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.seamoo.entities.Member;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchEvent;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.webapp.client.shared.ListenerMixin;
import org.seamoo.webapp.client.shared.ListenerMixin.Caller;
import org.seamoo.webapp.client.shared.ui.UiObjectFactory;
import org.seamoo.webapp.client.user.MatchBoard;
import org.seamoo.webapp.client.user.MatchBoard.Display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MatchView extends Composite implements Display {

	public interface MatchViewUiBinder extends UiBinder<Widget, MatchView> {
	}

	private static final MatchViewUiBinder binder = GWT.create(MatchViewUiBinder.class);
	@UiField
	HTMLPanel panelIndicator;
	@UiField
	HTMLPanel panelMatchWaiting;
	@UiField
	HTMLPanel panelYouFinished;
	@UiField
	InlineLabel labelMatchWaiting;
	@UiField
	HTMLPanel panelMatchPlayers;
	@UiField
	FlexTable tableMatchPlayers;
	@UiField
	HTMLPanel panelCountDown;
	@UiField
	Label labelCountDown;
	@UiField
	Label labelCountDownType;
	@UiField
	HTMLPanel panelQuestion;
	@UiField
	Label labelQuestionIndex;

	@UiField
	MultipleChoiceQuestionView multipleChoiceQuestionView;
	@UiField
	FollowPatternQuestionView followPatternQuestionView;

	@UiField
	Button buttonIgnore;
	@UiField
	Button buttonRematch;
	@UiField
	HTMLPanel panelLeaveMatch;
	@UiField
	Button buttonLeaveMatch;

	ListenerMixin<MatchBoard.Display.EventListener> listenerMixin;
	List<CompetitorView> competitorViews;

	private QuestionRevisionView.Listener getQuestionRevisionViewListener() {
		final MatchView me = this;
		return new QuestionRevisionView.Listener() {

			@Override
			public void submitAnswer(final String answer) {
				listenerMixin.each(new Caller<EventListener>() {

					@Override
					public void perform(EventListener listener) {
						listener.submitAnswer(me, answer);
					}

				});
			}
		};
	}

	public MatchView() {
		initWidget(binder.createAndBindUi(this));
		listenerMixin = new ListenerMixin<MatchBoard.Display.EventListener>();
		final MatchView me = this;
		multipleChoiceQuestionView.getListenerMixin().add(getQuestionRevisionViewListener());
		followPatternQuestionView.getListenerMixin().add(getQuestionRevisionViewListener());
		competitorViews = new ArrayList<CompetitorView>();
		buttonIgnore.addClickHandler(listenerMixin.getClickHandler(new Caller<EventListener>() {
			@Override
			public void perform(EventListener c) {
				c.ignoreQuestion(me);
			}
		}));

		buttonRematch.addClickHandler(listenerMixin.getClickHandler(new Caller<EventListener>() {
			@Override
			public void perform(EventListener c) {
				c.rematch(me);
			}
		}));

		buttonLeaveMatch.addClickHandler(listenerMixin.getClickHandler(new Caller<EventListener>() {
			@Override
			public void perform(EventListener c) {
				c.leaveMatch(me);
			}
		}));

		// Some panel is invisible by default
		panelQuestion.setVisible(false);
		panelIndicator.setVisible(false);
	}

	private void refreshCompetitorQuestionCount() {
		for (CompetitorView v : competitorViews) {
			v.setTotalQuestionsCount(questionTotal);
		}
	}

	@Override
	public void setCompetitors(List<MatchCompetitor> competitors, Map<Long, Member> membersMap) {
		if (tableMatchPlayers.getRowCount() != competitors.size()) {
			tableMatchPlayers.clear();
			for (int i = 0; i < competitors.size(); i++) {
				if (competitorViews.size() <= i) {
					CompetitorView view = UiObjectFactory.newCompetitorView();
					view.setTotalQuestionsCount(questionTotal);
					competitorViews.add(view);
				}
				tableMatchPlayers.setWidget(i, 0, competitorViews.get(i));
			}
			refreshCompetitorQuestionCount();
		}
		for (int j = 0; j < competitors.size(); j++) {
			competitorViews.get(j).setCompetitor(competitors.get(j), membersMap.get(competitors.get(j).getMemberAutoId()));
		}
	}

	@Override
	public void setPhase(MatchPhase phase) {
		panelIndicator.setVisible(true);
		panelMatchWaiting.setVisible(phase == MatchPhase.NOT_FORMED);
		panelMatchPlayers.setVisible(phase != MatchPhase.NOT_FORMED);
		panelCountDown.setVisible(phase == MatchPhase.FORMED || phase == MatchPhase.PLAYING || phase == MatchPhase.YOU_FINISHED);
		switch (phase) {
		case FORMED:
			labelCountDownType.setText("Chờ trận đấu bắt đầu");
			break;
		case PLAYING:
		case YOU_FINISHED:
			labelCountDownType.setText("Thời gian còn lại");
			break;

		}

		panelYouFinished.setVisible(phase == MatchPhase.YOU_FINISHED);
	}

	@Override
	public void setQuestion(Question question) {
		// TODO Auto-generated method stub
		panelQuestion.setVisible(question != null);
		if (question != null) {
			MultipleChoicesQuestionRevision revision = (MultipleChoicesQuestionRevision) question.getCurrentRevision();
			multipleChoiceQuestionView.setQuestionRevision(revision);
		}
	}

	private void submitAnswer(final String answer) {
		final MatchView me = this;
		listenerMixin.each(new Caller<EventListener>() {
			@Override
			public void perform(EventListener c) {
				c.submitAnswer(me, answer);
			}
		});
	}

	int questionIndex = 0, questionTotal = 0;
	public Object p;

	private void refreshQuestionIndicator() {
		// TODO Auto-generated method stub
		labelQuestionIndex.setText("" + questionIndex + "/" + questionTotal);
	}

	@Override
	public void setQuestionIndex(int index) {
		// TODO Auto-generated method stub
		questionIndex = index;
		refreshQuestionIndicator();
	}

	@Override
	public void setTotalQuestion(int total) {
		// TODO Auto-generated method stub
		questionTotal = total;
		refreshCompetitorQuestionCount();
		refreshQuestionIndicator();
	}

	@Override
	public void setRemainingTime(long seconds) {
		// TODO Auto-generated method stub
		long minutes = seconds / 60;
		seconds = seconds % 60;
		String s = minutes < 10 ? "0" + minutes : "" + minutes;
		s += ":";
		s += (seconds < 10 ? "0" + seconds : "" + seconds);
		labelCountDown.setText(s);
	}

	int eventIndex = -1;

	String memberToHTML(Member m) {
		// return "<a href='/users/" + m.getAutoId() + "/" + m.getAlias() +
		// "' target='_blank'>" + m.getDisplayName() + "</a>";
		return "<strong>" + m.getDisplayName() + "</strong>";
	}

	String eventToHTML(MatchEvent event) {
		switch (event.getType()) {
		case ANSWER_QUESTION:
			return memberToHTML(event.getMember()) + " answered question #" + event.getQuestionOrder();
		case IGNORE_QUESTION:
			return memberToHTML(event.getMember()) + " ignored question #" + event.getQuestionOrder();
		case JOINED:
			return memberToHTML(event.getMember()) + " joined match";
		case LEFT:
			return memberToHTML(event.getMember()) + " left match";
		case STARTED:
			return "Match started";
		case FINISHED:
			return "Match finished";
		default:
			throw new IllegalArgumentException("MatchEvent type is not supported");
		}
	}

	@Override
	public Widget getSideWidget() {
		return panelIndicator;
	}

	@Override
	public ListenerMixin<EventListener> getListenerMixin() {
		return listenerMixin;
	}

}
