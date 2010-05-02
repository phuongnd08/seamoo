package org.seamoo.daos.twigImpl.lookup;

import static org.testng.Assert.*;

import org.seamoo.daos.lookup.NumericBagDao;
import org.seamoo.lookup.NumericBag;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TwigNumericBagDaoImplTest extends LocalAppEngineTest {

	public TwigNumericBagDaoImplTest() {
		// TODO Auto-generated constructor stub
	}

	NumericBagDao dao;

	@BeforeMethod
	@Override
	public void setUp() {
		super.setUp();
		dao = new TwigNumericBagDaoImpl();
	}

	@AfterMethod
	@Override
	public void tearDown() {
		super.tearDown();
	}

	@Test
	public void sizeIsCorrectlyReflected() {
		NumericBag bag = new NumericBag();
		assertEquals(bag.getKeyList().size(), 0);
		bag.addKey(1L);
		bag.addKey(2L);
		assertEquals(bag.getKeyList().size(), 2);
		bag.removeKey(1L);
		assertEquals(bag.getKeyList().size(), 1);
	}

	@Test
	public void reloadedNumericBagReflectChangeWhenAddKey() {
		NumericBag bag = new NumericBag();
		bag.addKey(1L);
		bag.addKey(2L);
		dao.persist(bag);
		NumericBag reloadedBag = dao.findByKey(bag.getAutoId());
		assertEquals(reloadedBag.getKeyList().size(), 2);
		assertEquals(reloadedBag.getKeyList().get(0).longValue(), 1L);
		assertEquals(reloadedBag.getKeyList().get(1).longValue(), 2L);
	}

	@Test
	public void reloadedNumericBagReflectChangeWhenRemoveKey() {
		NumericBag bag = new NumericBag();
		bag.addKey(1L);
		bag.addKey(2L);
		bag.removeKey(1L);
		dao.persist(bag);
		NumericBag reloadedBag = dao.findByKey(bag.getAutoId());
		assertEquals(reloadedBag.getKeyList().size(), 1);
		assertEquals(reloadedBag.getKeyList().get(0).longValue(), 2L);
	}

	@Test
	public void reloadedTwiceBagReflectChangeWhenAndAndRemoveKey() {
		NumericBag bag = new NumericBag();
		bag.addKey(1L);
		bag.addKey(2L);
		bag.removeKey(1L);
		dao.persist(bag);
		NumericBag reloadedBag = dao.findByKey(bag.getAutoId());
		reloadedBag.addKey(3L);
		reloadedBag.addKey(4L);
		reloadedBag.removeKey(2L);
		dao.persist(reloadedBag);
		NumericBag reloadedTwiceBag = dao.findByKey(bag.getAutoId());
		assertEquals(reloadedTwiceBag.getKeyList().size(), 2);
		assertEquals(reloadedTwiceBag.getKeyList().get(0).longValue(), 3L);
		assertEquals(reloadedTwiceBag.getKeyList().get(1).longValue(), 4L);
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
