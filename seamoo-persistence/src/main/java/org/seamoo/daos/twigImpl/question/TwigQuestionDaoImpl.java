package org.seamoo.daos.twigImpl.question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.seamoo.daos.lookup.NumericBagDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.daos.twigImpl.TwigGenericDaoImpl;
import org.seamoo.daos.twigImpl.lookup.TwigNumericBagDaoImpl;
import org.seamoo.entities.question.Question;
import org.seamoo.lookup.NumericBag;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.common.collect.Lists;
import com.vercer.engine.persist.FindCommand.RootFindCommand;
import com.vercer.engine.persist.util.generic.GenericTypeReflector;

public class TwigQuestionDaoImpl extends TwigGenericDaoImpl<Question, Long> implements QuestionDao {

	private Random rndGenerator = new Random();
	private NumericBagDao numericBagDao;
	private NumericBag autoIdBag;
	private final String classifier = Question.class.getCanonicalName();

	public TwigQuestionDaoImpl() {
		numericBagDao = new TwigNumericBagDaoImpl();
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
			shouldAddKey[i] = entities[i].getAutoId() == null;
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
		int totalSize = autoIdBag.getKeyList().size();
		if (number > totalSize) {
			throw new IllegalArgumentException(String.format("%d exceeds the number of available questions", number));
		}
		Set<Long> pickedAutoIds = new HashSet<Long>();
		int pickedSize = 0;
		while (pickedAutoIds.size() < number) {
			Long value = autoIdBag.getKeyList().get(rndGenerator.nextInt(totalSize));
			if (!pickedAutoIds.contains(value)) {
				pickedAutoIds.add(value);
				pickedSize++;
			}
		}

		List<Question> result = new ArrayList<Question>();

		for (Long autoId : pickedAutoIds)
			result.add(getOds().load(Question.class, autoId));
		return result;
	}

	@Override
	public void delete(Question entity) {
		// TODO Auto-generated method stub
		autoIdBag.removeKey(entity.getAutoId());
		super.delete(entity);
		numericBagDao.persist(autoIdBag);
	}

}
