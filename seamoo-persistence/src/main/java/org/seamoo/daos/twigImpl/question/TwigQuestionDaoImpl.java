package org.seamoo.daos.twigImpl.question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.seamoo.daos.lookup.NumericBagDao;
import org.seamoo.daos.question.QuestionDao;
import org.seamoo.daos.twigImpl.TwigGenericDaoImpl;
import org.seamoo.daos.twigImpl.lookup.TwigNumericBagDaoImpl;
import org.seamoo.entities.question.Question;
import org.seamoo.lookup.NumericBag;

public class TwigQuestionDaoImpl extends TwigGenericDaoImpl<Question, Long> implements QuestionDao {

	private Random rndGenerator = new Random();
	private NumericBagDao numericBagDao;
	private final String classifierPrefix = Question.class.getCanonicalName();
	private Map<Long, NumericBag> bagByLeagueId;

	private NumericBag getNumericBagByLeagueId(Long leagueId) {
		if (!bagByLeagueId.containsKey(leagueId)) {
			String cfier = classifierPrefix + "@" + leagueId;
			NumericBag b = numericBagDao.findByClassifier(cfier);
			if (b == null)
				b = new NumericBag(cfier);
			bagByLeagueId.put(leagueId, b);
			return b;
		}
		return bagByLeagueId.get(leagueId);
	}

	public TwigQuestionDaoImpl() {
		numericBagDao = new TwigNumericBagDaoImpl();
		bagByLeagueId = new HashMap<Long, NumericBag>();
	}

	@Override
	public Question persist(Question entity) {
		// TODO Auto-generated method stub
		boolean shouldAddKey = entity.getAutoId() == null;
		Question q = super.persist(entity);
		if (shouldAddKey) {
			NumericBag bag = getNumericBagByLeagueId(entity.getLeagueAutoId());
			bag.addNumber(q.getAutoId());
			numericBagDao.persist(bag);
		}
		return q;
	}

	@Override
	public Question[] persist(Question[] entities) {
		// TODO Auto-generated method stub
		boolean[] shouldAddKey = new boolean[entities.length];
		Map<Long, Boolean> shouldPersistKeyBag = new HashMap<Long, Boolean>();
		for (int i = 0; i < entities.length; i++) {
			shouldAddKey[i] = entities[i].getAutoId() == null;
			shouldPersistKeyBag.put(entities[i].getLeagueAutoId(), shouldAddKey[i]);
		}
		Question[] qs = super.persist(entities);
		for (int i = 0; i < qs.length; i++) {
			if (shouldAddKey[i])
				getNumericBagByLeagueId(qs[i].getLeagueAutoId()).addNumber(qs[i].getAutoId());
		}

		for (Long leagueId : shouldPersistKeyBag.keySet())
			numericBagDao.persist(getNumericBagByLeagueId(leagueId));
		return qs;
	}

	public List<Question> getRandomQuestions(Long leagueId, int number) {
		NumericBag autoIdBag = getNumericBagByLeagueId(leagueId);
		int totalSize = autoIdBag.getSize();
		if (number > totalSize) {
			throw new IllegalArgumentException(String.format("%d exceeds the number of available questions", number));
		}
		Set<Long> pickedAutoIds = new HashSet<Long>();
		int pickedSize = 0;
		while (pickedAutoIds.size() < number) {
			Long value = autoIdBag.get(rndGenerator.nextInt(totalSize));
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
		NumericBag autoIdBag = getNumericBagByLeagueId(entity.getLeagueAutoId());
		autoIdBag.removeNumber(entity.getAutoId());
		super.delete(entity);
		numericBagDao.persist(autoIdBag);
	}

}
