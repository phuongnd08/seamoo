package org.seamoo.daos.twigImpl.speed;

import static org.testng.Assert.*;

import java.util.ArrayList;

import org.seamoo.daos.speed.NumericBagDao;
import org.seamoo.daos.twigImpl.ObjectDatastoreProvider;
import org.seamoo.daos.twigImpl.speed.TwigNumericBagDaoImpl;
import org.seamoo.lookup.NumericBag;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TwigNumericBagDaoImplTest extends LocalAppEngineTest {

	public TwigNumericBagDaoImplTest() {
		// TODO Auto-generated constructor stub
	}

	NumericBagDao dao;
	NumericBag bag;

	@BeforeMethod
	@Override
	public void setUp() {
		super.setUp();
		dao = new TwigNumericBagDaoImpl();
		bag = new NumericBag();
		bag.addNumber(1L);
		bag.addNumber(2L);
		bag.addNumber(4L);

	}

	@AfterMethod
	@Override
	public void tearDown() {
		super.tearDown();
	}

	void assertBagCollections(NumericBag bag, String name, Object[] values) {
		ArrayList<Long> list = (ArrayList<Long>) ReflectionTestUtils.getField(bag, name);
		assertEquals(list.toArray(), values);
	}

	void assertAllBagCollections(NumericBag bag, Object[] heads, Object[] tails, Object[] indices) {
		assertBagCollections(bag, "heads", heads);
		assertBagCollections(bag, "tails", tails);
		assertBagCollections(bag, "indices", indices);
	}

	@Test
	public void bagSegmentsShouldBeCorrectlyPersisted() {
		dao.persist(bag);
		TwigNumericBagDaoImpl newDaoImpl = new TwigNumericBagDaoImpl();
		ReflectionTestUtils.setField(newDaoImpl, "objectDatastoreProvider", new ObjectDatastoreProvider());
		NumericBag reloadedBag = newDaoImpl.findByKey(bag.getAutoId());
		assertAllBagCollections(reloadedBag, new Object[] { 1L, 4L }, new Object[] { 2L, 4L }, new Object[] { 0L, 2L });
	}

	@Test
	public void loadByClassifierShouldReturnMatchedBag() {
		NumericBag bag = new NumericBag();
		bag.setClassifier("Question");
		dao.persist(bag);
		NumericBag reloadedBag = dao.findByClassifier("Question");
		assertEquals(reloadedBag.getClassifier(), "Question");
	}

	@Test
	public void loadByClassifierShouldReturnNullForUnavailableBag() {
		NumericBag bag = dao.findByClassifier("Question xxx");
		assertNull(bag);
	}
}
