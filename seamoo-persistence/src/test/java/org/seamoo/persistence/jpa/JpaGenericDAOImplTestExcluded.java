package org.seamoo.persistence.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.seamoo.persistence.test.LocalDatastoreTest;
import org.seamoo.persistence.test.jpa.model.ExampleModel;

public class JpaGenericDAOImplTestExcluded extends LocalDatastoreTest {

	private class TestModelDAOImpl extends JpaGenericDAOImpl<ExampleModel, Long> {

	}

	@Override
	@Before
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
	}

	@Override
	@After
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
	public void loadMethodShouldReproduceEntity() {
		ExampleModel testModel = new ExampleModel();
		testModel.setField("Ronaldo");
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.persist(testModel);
		ExampleModel reloadedTestModel = daoImpl.findById(new Long(1));
		assertEquals("Ronaldo", reloadedTestModel.getField());
	}

	@Test
	public void persistMultipleEntitiesProduceMultipleIdentity() {
		ExampleModel[] testModels = new ExampleModel[] { new ExampleModel(),
				new ExampleModel() };
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.persist(testModels);
		assertEquals(1, testModels[0].getAutoId());
		assertEquals(2, testModels[1].getAutoId());
	}

	@Test
	public void loadAllShouldReturnAllEntities() {
		ExampleModel[] testModels = new ExampleModel[] { new ExampleModel(),
				new ExampleModel() };
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.persist(testModels);
		assertEquals(2, daoImpl.getAll().size());
	}
}
