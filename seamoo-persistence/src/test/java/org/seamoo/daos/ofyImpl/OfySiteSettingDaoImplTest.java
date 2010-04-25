package org.seamoo.daos.ofyImpl;

import static org.testng.Assert.*;

import org.seamoo.daos.SiteSettingDao;
import org.seamoo.entities.SiteSetting;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class OfySiteSettingDaoImplTest extends LocalAppEngineTest {
	SiteSettingDao siteSettingDAO;

	public OfySiteSettingDaoImplTest() {
		new OfyModelRegistrar();// register object for testing.
	}

	@Override
	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		siteSettingDAO = new OfySiteSettingDaoImpl();
	}

	@Override
	@AfterMethod
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@Test
	public void getNonExistentSettingShouldReturnNull() {
		assertEquals(null, siteSettingDAO.getSetting("nothing"));
	}

	@Test
	public void getExistentSettingShouldReturnCorrespondingValue() {
		SiteSetting setting = new SiteSetting("model", "bootstrap");
		siteSettingDAO.persist(setting);
		assertEquals("bootstrap", siteSettingDAO.getSetting("model"));
	}

	@Test
	public void assignNonExistentSettingShouldCreateNewEntity() {
		siteSettingDAO.assignSetting("model", "bootstrap");
		assertNotNull(siteSettingDAO.findByKey("model"));
	}

	@Test
	public void assignExistentSettingShouldUpdateExistingEntity() {
		SiteSetting setting = new SiteSetting("model", "bootstrap");
		siteSettingDAO.persist(setting);
		siteSettingDAO.assignSetting("model", "normal");
		assertEquals("normal", siteSettingDAO.findByKey("model").getValue());
	}

	@Test
	public void assignExistentSettingShouldNotThrowException() {
		SiteSetting setting = new SiteSetting("model", "bootstrap");
		siteSettingDAO.persist(setting);
		siteSettingDAO.assignSetting("model", "normal");
	}

}
