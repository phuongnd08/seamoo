package org.seamoo.daos.question;

import java.util.List;

import org.seamoo.daos.GenericDao;
import org.seamoo.entities.question.Question;

public interface QuestionDao extends GenericDao<Question, Long> {

	List<Long> getRandomQuestionKeys(Long leagueId, int number);

}
