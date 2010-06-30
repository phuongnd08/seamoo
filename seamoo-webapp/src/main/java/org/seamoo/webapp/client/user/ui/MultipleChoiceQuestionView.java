package org.seamoo.webapp.client.user.ui;

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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MultipleChoiceQuestionView extends Composite implements QuestionRevisionView {

	public interface MultipleChoiceQuestionViewUiBinder extends UiBinder<Widget, MultipleChoiceQuestionView> {
	}

	private static final MultipleChoiceQuestionViewUiBinder binder = GWT.create(MultipleChoiceQuestionViewUiBinder.class);
	@UiField
	Label labelQuestion;
	@UiField
	FlexTable tableChoices;

	ListenerMixin<QuestionRevisionView.Listener> listenerMixin;

	public MultipleChoiceQuestionView() {
		initWidget(binder.createAndBindUi(this));
		listenerMixin = new ListenerMixin<QuestionRevisionView.Listener>();
	}

	public static final int AVATAR_SIZE = 32;

	public void setQuestionRevision(QuestionRevision revision) {
		MultipleChoicesQuestionRevision mcRevision = (MultipleChoicesQuestionRevision) revision;
		labelQuestion.setText(mcRevision.getContent());
		tableChoices.clear();
		int i = 0;
		for (QuestionChoice choice : mcRevision.getChoices()) {
			tableChoices.setWidget(i, 0, createChoiceButton(choice.getContent(), i + 1));
			// answer for multiple choice
			// should be in 1-based form
			i++;
		}
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
