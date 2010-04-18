package org.seamoo.daos.jdoImpl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.seamoo.daos.jdoImpl.JdoPersistenceHelper;
import org.seamoo.persistence.test.jpa.model.ExampleModel;

public class JdoPersistenceHelperTest {

	@Test
	public void getPrimaryKeyReturnPrimaryKey() {
		ExampleModel exModel = new ExampleModel();
		exModel.setAutoId(new Long(100));
		assertEquals(100L, JdoPersistenceHelper.findPrimaryKey(exModel));
	}

	@Test
	public void copyPersistentFieldsShouldProduceIdenticalObjects() {
		ExampleModel modelFrom = new ExampleModel();
		modelFrom.setAutoId(new Long(100));
		modelFrom.setField("Hello");
		ExampleModel modelTo = new ExampleModel();
		JdoPersistenceHelper.copyPersistentFields(modelFrom, modelTo);
		assertEquals(100L, modelTo.getAutoId());
		assertEquals("Hello", modelTo.getField());
	}
}
