package org.seamoo.webapp.client.user;

import java.util.List;

import org.seamoo.entities.League;
import org.seamoo.entities.Subject;
import org.seamoo.entities.question.Question;
import org.workingonit.gwtbridge.GwtRemoteService;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@GwtRemoteService("/user-service/question")
@RemoteServiceRelativePath("../user-service/question")
public interface UserQuestionService extends RemoteService {
	List<Subject> loadAllEnabledSubjects();

	List<League> loadAllEnabledLeagues(Long subjectId);

	Question create(Long leagueId, Question question);
}
