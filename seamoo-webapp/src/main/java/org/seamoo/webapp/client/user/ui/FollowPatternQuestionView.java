package org.seamoo.webapp.client.user.ui;

import org.seamoo.entities.question.FollowPatternQuestionRevision;
import org.seamoo.entities.question.QuestionRevision;
import org.seamoo.webapp.client.shared.ListenerMixin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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

	ListenerMixin<QuestionRevisionView.Listener> listenerMixin;

	public FollowPatternQuestionView() {
		initWidget(binder.createAndBindUi(this));
		listenerMixin = new ListenerMixin<QuestionRevisionView.Listener>();
	}

	public static final int AVATAR_SIZE = 32;
	public static final String GUIDING_UNTYPED_STYLE_NAME = "guiding_untyped";
	public static final String GUIDING_WRONG_STYLE_NAME = "guiding_wrong";
	public static final String GUIDING_RIGHT_STYLE_NAME = "guiding_right";

	public void setQuestionRevision(QuestionRevision revision) {
		FollowPatternQuestionRevision fpRevision = (FollowPatternQuestionRevision) revision;
		labelQuestion.setText(fpRevision.getContent());
		setGuidingPattern(fpRevision.getGuidingPattern());
	}

	private void setGuidingPattern(String guidingPattern) {
		tableGuidingPattern.clear();
		for (int i = 0; i < guidingPattern.length(); i++) {
			tableGuidingPattern.setHTML(0, i, "<span class='" + GUIDING_UNTYPED_STYLE_NAME + "'>" + i + "</span>");
		}
	}

	@Override
	public ListenerMixin<Listener> getListenerMixin() {
		return listenerMixin;
	}
}
