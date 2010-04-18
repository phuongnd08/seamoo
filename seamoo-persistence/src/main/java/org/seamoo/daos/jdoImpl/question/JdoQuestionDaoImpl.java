package org.seamoo.daos.jdoImpl.question;

import java.util.List;
import java.util.Random;

import org.seamoo.daos.jdoImpl.JdoGenericDaoImpl;
import org.seamoo.daos.jdoImpl.lookup.JdoNumericBagDaoImpl;
import org.seamoo.daos.lookup.NumericBagDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.entities.question.Question;
import org.seamoo.lookup.NumericBag;

public class JdoQuestionDaoImpl extends JdoGenericDaoImpl<Question, Long> implements QuestionDao {

	private Random rndGenerator = new Random();
	private NumericBagDao numericBagDao;
	private NumericBag autoIdBag;
	private final String classifier = Question.class.getCanonicalName();

	public JdoQuestionDaoImpl() {
		numericBagDao = new JdoNumericBagDaoImpl();
		List<NumericBag> bags = numericBagDao.findByClassifier(classifier);
		if (bags.size() == 0) {
			autoIdBag = new NumericBag();
			autoIdBag.setClassifier(classifier);
		} else {
			autoIdBag = bags.get(0);
		}
	}

	@Override
	public Question persist(Question entity) {
		// TODO Auto-generated method stub
		boolean shouldAddKey = entity.getAutoId() == null;
		Question q = super.persist(entity);
		if (shouldAddKey) {
			autoIdBag.addKey(q.getAutoId());
			numericBagDao.persist(autoIdBag);
		}
		return q;
	}

	@Override
	public Question[] persist(Question[] entities) {
		// TODO Auto-generated method stub
		boolean[] shouldAddKey = new boolean[entities.length];
		boolean shouldPersistKeyBag = false;
		for (int i = 0; i < entities.length; i++) {
			shouldAddKey[i] = entities[i].getAutoId() != null;
			shouldPersistKeyBag = shouldPersistKeyBag || shouldAddKey[i];
		}
		Question[] qs = super.persist(entities);
		for (int i = 0; i < qs.length; i++) {
			if (shouldAddKey[i])
				autoIdBag.addKey(qs[i].getAutoId());
		}
		if (shouldPersistKeyBag)
			numericBagDao.persist(autoIdBag);
		return qs;
	}

	public List<Question> getRandomQuestions(int number) {
		return null;
	}

	@Override
	public void delete(Question entity) {
		// TODO Auto-generated method stub
		autoIdBag.removeKey(entity.getAutoId());
		super.delete(entity);
		numericBagDao.persist(autoIdBag);
	}
}
