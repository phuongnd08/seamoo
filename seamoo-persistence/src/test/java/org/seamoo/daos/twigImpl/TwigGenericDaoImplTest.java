package org.seamoo.daos.twigImpl;

import static org.testng.Assert.*;

import org.seamoo.persistence.test.LocalDatastoreTest;
import org.seamoo.persistence.test.jpa.model.ExampleModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TwigGenericDaoImplTest extends LocalDatastoreTest {

	private class TestModelDAOImpl extends TwigGenericDaoImpl<ExampleModel, Long> {

	}

	@Override
	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
	}

	@Override
	@AfterMethod
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@Test
	public void persistMethodShouldGenerateIdentityForEntity() {
		ExampleModel testModel = new ExampleModel();
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.persist(testModel);
		assertEquals(1, testModel.getAutoId());
	}

	@Test
	public void retrieveOfNonExistentObjectReturnNull() {
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		assertEquals(null, daoImpl.findByKey(new Long(10000)));
	}

	@Test
	public void loadMethodShouldReproduceEntity() {
		ExampleModel testModel = new ExampleModel();
		testModel.setField("Ronaldo");
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.persist(testModel);
		ExampleModel reloadedTestModel = daoImpl.findByKey(new Long(1));
		assertEquals("Ronaldo", reloadedTestModel.getField());
	}

	@Test
	public void persistMultipleEntitiesProduceMultipleIdentity() {
		ExampleModel[] testModels = new ExampleModel[] { new ExampleModel(), new ExampleModel() };
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.persist(testModels);
		assertEquals(1, testModels[0].getAutoId());
		assertEquals(2, testModels[1].getAutoId());
	}

	@Test
	public void loadAllShouldReturnAllEntities() {
		ExampleModel[] testModels = new ExampleModel[] { new ExampleModel(), new ExampleModel() };
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.persist(testModels);
		assertEquals(2, daoImpl.getAll().size());
	}

	@Test
	public void deleteTransientEntityShouldBeOK() {
		ExampleModel model = new ExampleModel();
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.persist(model);
		ExampleModel transientModel = new ExampleModel();
		transientModel.setAutoId(model.getAutoId());
		daoImpl.delete(transientModel);
		assertEquals(null, daoImpl.findByKey(model.getAutoId()));
	}
}
