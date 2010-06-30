package org.seamoo.webapp.client.user.ui;

import org.seamoo.entities.question.QuestionRevision;
import org.seamoo.webapp.client.shared.ListenerMixin;

public interface QuestionRevisionView {
	public static interface Listener{
		void submitAnswer(String answer);
	}
	void setQuestionRevision(QuestionRevision revision);
	ListenerMixin<Listener> getListenerMixin();
}
