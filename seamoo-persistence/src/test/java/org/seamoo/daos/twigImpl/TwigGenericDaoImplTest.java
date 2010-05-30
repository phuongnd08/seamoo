package org.seamoo.daos.twigImpl;

import static org.testng.Assert.*;

import java.util.List;

import org.seamoo.persistence.test.LocalAppEngineTest;
import org.seamoo.persistence.test.jpa.model.ExampleModel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.googlecode.objectify.ObjectifyService;

public class TwigGenericDaoImplTest extends LocalAppEngineTest {

	private class TestModelDAOImpl extends TwigGenericDaoImpl<ExampleModel, Long> {
	}

	public TwigGenericDaoImplTest() {
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
	public void persistMethodTwiceShouldNotRegenerateIdentity() {
		ExampleModel testModel = new ExampleModel();
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.persist(testModel);
		testModel.setField("Wow");
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
	public void deleteEntityRemoveEntityFromDataStore() {
		ExampleModel model = new ExampleModel();
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.persist(model);
		long modelKey = model.getAutoId();
		daoImpl.delete(model);
		assertEquals(null, daoImpl.findByKey(modelKey));
	}

	@Test
	public void persistEmptyListShouldBeOK() {
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.persist(new ExampleModel[0]);

	}

	@Test
	public void countAllShouldReturnNumberOfPersistedEntities() {
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		assertEquals(daoImpl.countAll(), 0);
		daoImpl.persist(new ExampleModel());
		assertEquals(daoImpl.countAll(), 1);
		daoImpl.persist(new ExampleModel());
		assertEquals(daoImpl.countAll(), 2);
	}

	@Test
	public void getSubSetReturnSubSet() {
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		ExampleModel[] models = new ExampleModel[] { new ExampleModel(), new ExampleModel(), new ExampleModel() };
		daoImpl.persist(models);
		List<ExampleModel> subSet = daoImpl.getSubSet(1, 2);
		assertEquals(2, subSet.size());
		assertEquals(subSet.get(0).getAutoId(), models[1].getAutoId());
	}

	@Test
	public void transientFieldIsStillStored() {
		TestModelDAOImpl daoImpl = new TestModelDAOImpl();
		daoImpl.objectDatastoreProvider = new ObjectDatastoreProvider();
		ExampleModel model = new ExampleModel();
		model.setTransientField("xxx");
		daoImpl.persist(model);
		TestModelDAOImpl newDaoImpl = new TestModelDAOImpl();
		newDaoImpl.objectDatastoreProvider = new ObjectDatastoreProvider();
		ExampleModel reloadedModel = newDaoImpl.findByKey(model.getAutoId());
		assertEquals(reloadedModel.getTransientField(), "xxx");
	}
}
