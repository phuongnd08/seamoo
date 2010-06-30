package org.seamoo.webapp.client.user.ui;

import org.seamoo.entities.question.FollowPatternQuestionRevision;
import org.seamoo.entities.question.MultipleChoicesQuestionRevision;
import org.seamoo.entities.question.QuestionChoice;
import org.seamoo.entities.question.QuestionRevision;
import org.seamoo.webapp.client.shared.ListenerMixin;
import org.seamoo.webapp.client.shared.ListenerMixin.Caller;
import org.seamoo.webapp.client.shared.ui.UiObjectFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
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
	Label labelGuidingPattern;
	@UiField
	TextBox textboxAnswer;

	ListenerMixin<QuestionRevisionView.Listener> listenerMixin;

	public FollowPatternQuestionView() {
		initWidget(binder.createAndBindUi(this));
		listenerMixin = new ListenerMixin<QuestionRevisionView.Listener>();
	}

	public static final int AVATAR_SIZE = 32;

	public void setQuestionRevision(QuestionRevision revision) {
		FollowPatternQuestionRevision fpRevision = (FollowPatternQuestionRevision) revision;
		labelQuestion.setText(fpRevision.getContent());
		labelGuidingPattern.setText(fpRevision.getPattern());
	}

	private Button createChoiceButton(String choice, final int index) {
		Button b = UiObjectFactory.newButton();
		b.setText(choice);
		b.addClickHandler(listenerMixin.getClickHandler(new Caller<QuestionRevisionView.Listener>() {

			@Override
			public void perform(QuestionRevisionView.Listener c) {
				c.submitAnswer(String.valueOf(index));
			}
		})); 
		return b;
	}

	@Override
	public ListenerMixin<Listener> getListenerMixin() {
		return listenerMixin;
	}
}
