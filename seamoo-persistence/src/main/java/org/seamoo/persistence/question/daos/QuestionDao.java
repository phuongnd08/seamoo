package org.seamoo.persistence.question.daos;

import java.util.List;

import org.seamoo.entities.question.Question;
import org.seamoo.persistence.daos.GenericDao;

public interface QuestionDao extends GenericDao<Question, Long> {

	List<Question> getRandomQuestions(int number);

}
