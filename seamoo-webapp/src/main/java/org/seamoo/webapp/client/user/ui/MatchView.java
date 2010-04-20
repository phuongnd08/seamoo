package org.seamoo.webapp.client.user.ui;

import java.util.ArrayList;
import java.util.List;

import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.entities.matching.MatchEvent;
import org.seamoo.entities.matching.MatchPhase;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.Question;
import org.seamoo.entities.question.QuestionChoice;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MatchView extends Composite implements Display {

	public interface MatchViewUiBinder extends UiBinder<Widget, MatchView> {
	}

	private static final MatchViewUiBinder binder = GWT.create(MatchViewUiBinder.class);
	@UiField
	HTMLPanel panelIndicator;
	@UiField
	FlowPanel panelMatchStatus;
	@UiField
	Image imgMatchStatus;
	@UiField
	Image labelMatchStatus;
	@UiField
	FlowPanel panelMatchPlayers;
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
	Label labelQuestion;
	@UiField
	FlowPanel panelChoices;
	@UiField
	Button buttonIgnore;
	@UiField
	HTMLPanel panelEvents;
	@UiField
	FlexTable tableEvents;

	List<MatchBoard.Display.EventListener> listeners;

	public MatchView() {
		initWidget(binder.createAndBindUi(this));
		listeners = new ArrayList<EventListener>();
		final MatchView me = this;
		buttonIgnore.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub
				for (EventListener l : listeners) {
					l.ignoreQuestion(me);
				}
			}
		});

		// Some panel is invisible by default
		panelQuestion.setVisible(false);
	}

	@Override
	public void addEventListener(EventListener listener) {
		// TODO Auto-generated method stub
		listeners.add(listener);
	}

	@Override
	public void setCompetitors(List<MatchCompetitor> competitors) {
		// TODO Auto-generated method stub
		tableMatchPlayers.clear();
		int i = 0;
		for (MatchCompetitor competitor : competitors) {
			tableMatchPlayers.setText(i, 0, competitor.getMember().getDisplayName());
			i++;
		}
	}

	@Override
	public void setPhase(MatchPhase phase) {
		panelMatchStatus.setVisible(phase == MatchPhase.NOT_FORMED);
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
	}

	@Override
	public void setQuestion(Question question) {
		// TODO Auto-generated method stub
		panelQuestion.setVisible(question != null);
		if (question != null) {
			MultipleChoicesQuestionRevision revision = (MultipleChoicesQuestionRevision) question.getCurrentRevision();
			labelQuestion.setText(revision.getContent());
			panelChoices.clear();
			int i = 0;
			for (QuestionChoice choice : revision.getChoices()) {
				panelChoices.add(createChoiceButton(choice.getContent(), i));
				i++;
			}
		}
	}

	private Button createChoiceButton(String choice, final int index) {
		Button b = UiObjectFactory.newButton();
		b.setText(choice);
		b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				// TODO Auto-generated method stub
				submitAnswer(String.valueOf(index));
			}

		});
		return b;
	}

	private void submitAnswer(String answer) {
		// TODO Auto-generated method stub
		for (EventListener l : listeners)
			l.submitAnswer(this, answer);
	}

	int questionIndex = 0, questionTotal = 0;
	public Object p;

	@Override
	public void setQuestionIndex(int index) {
		// TODO Auto-generated method stub
		questionIndex = index;
		refreshQuestionIndicator();
	}

	private void refreshQuestionIndicator() {
		// TODO Auto-generated method stub
		labelQuestionIndex.setText("" + questionIndex + "/" + questionTotal);
	}

	@Override
	public void setTotalQuestion(int total) {
		// TODO Auto-generated method stub
		questionTotal = total;
		refreshQuestionIndicator();

	}

	@Override
	public void setRemainingTime(int seconds) {
		// TODO Auto-generated method stub
		int minutes = seconds / 60;
		seconds = seconds % 60;
		String s = minutes < 10 ? "0" + minutes : "" + minutes;
		s += ":";
		s += (seconds < 10 ? "0" + seconds : "" + seconds);
		labelCountDown.setText(s);
	}

	@Override
	public void addEvents(List<MatchEvent> events) {
		// TODO Auto-generated method stub

	}
}
