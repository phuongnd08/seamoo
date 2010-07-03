package org.seamoo.webapp.client.user.ui;

import org.seamoo.entities.question.FollowPatternQuestionRevision;
import org.seamoo.entities.question.QuestionRevision;
import org.seamoo.webapp.client.shared.ListenerMixin;
import org.seamoo.webapp.client.shared.ListenerMixin.Caller;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class FollowPatternQuestionView extends Composite implements QuestionRevisionView {
	public interface FollowPatternQuestionViewUiBinder extends UiBinder<Widget, FollowPatternQuestionView> {
	}

	private static final FollowPatternQuestionViewUiBinder binder = GWT.create(FollowPatternQuestionViewUiBinder.class);
	@UiField
	Label labelQuestion;
	@UiField
	FlexTable tableGuidingPattern;
	@UiField
	TextBox textboxAnswer;
	@UiField
	Button buttonSubmitAnswer;

	private FollowPatternQuestionRevision revision;

	ListenerMixin<QuestionRevisionView.Listener> listenerMixin;

	public FollowPatternQuestionView() {
		initWidget(binder.createAndBindUi(this));
		listenerMixin = new ListenerMixin<QuestionRevisionView.Listener>();
		textboxAnswer.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				refreshGuidingPattern();
			}
		});

		textboxAnswer.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == 13)
					submitAnswer();
			}
		});

		textboxAnswer.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent arg0) {
				refreshGuidingPattern();
			}
		});

		buttonSubmitAnswer.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				submitAnswer();
			}
		});
	}

	public static final int AVATAR_SIZE = 32;
	public static final String GUIDING_UNTYPED_STYLE_NAME = "guiding_untyped";
	public static final String GUIDING_WRONG_STYLE_NAME = "guiding_wrong";
	public static final String GUIDING_RIGHT_STYLE_NAME = "guiding_right";

	public void setQuestionRevision(QuestionRevision revision) {
		this.revision = (FollowPatternQuestionRevision) revision;
		labelQuestion.setText(this.revision.getContent());
		textboxAnswer.setText("");
		refreshGuidingPattern();
	}

	@Override
	public ListenerMixin<Listener> getListenerMixin() {
		return listenerMixin;
	}

	private void refreshGuidingPattern() {
		String answer = textboxAnswer.getText();
		String guidingPattern = revision.getGuidingPattern();
		tableGuidingPattern.clear(true);
		int len = Math.max(guidingPattern.length(), answer.length());
		for (int i = 0; i < len; i++) {
			String spanClass = null;
			String ch;
			if (i < guidingPattern.length()) {
				if (i < answer.length())
					if (guidingPattern.charAt(i) == '-')
						spanClass = GUIDING_RIGHT_STYLE_NAME;
					else if (guidingPattern.charAt(i) == answer.charAt(i))
						spanClass = GUIDING_RIGHT_STYLE_NAME;
					else
						spanClass = GUIDING_WRONG_STYLE_NAME;
				else
					spanClass = GUIDING_UNTYPED_STYLE_NAME;
				ch = String.valueOf(guidingPattern.charAt(i));
			} else {
				spanClass = GUIDING_WRONG_STYLE_NAME;
				ch = String.valueOf(answer.charAt(i));
			}
			if (ch.equals(" "))
				if (spanClass == GUIDING_WRONG_STYLE_NAME) {
					ch = String.valueOf(answer.charAt(i));
					if (ch.equals(" "))
						ch = "_";
				} else
					ch = "&nbsp;";
			tableGuidingPattern.setHTML(0, i, "<span class='" + spanClass + "'>" + ch + "</span>");
		}
	}

	private void submitAnswer() {
		listenerMixin.each(new Caller<Listener>() {
			@Override
			public void perform(Listener listener) {
				listener.submitAnswer(textboxAnswer.getText());
			}
		});
	}
}
