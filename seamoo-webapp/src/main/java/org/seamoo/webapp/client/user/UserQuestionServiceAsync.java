package org.seamoo.webapp.client.user;

import java.util.List;

import org.seamoo.entities.League;
import org.seamoo.entities.Subject;
import org.seamoo.entities.question.Question;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserQuestionServiceAsync {
	public void loadAllEnabledSubjects(AsyncCallback<List<Subject>> callback);

	public void loadAllEnabledLeagues(Long subjectId, AsyncCallback<List<League>> callback);

	public void create(Long leagueId, Question question, AsyncCallback<Question> callback);
}
