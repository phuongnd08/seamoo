package org.seamoo.persistence.daos.jdo;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seamoo.entities.SiteSetting;
import org.seamoo.persistence.daos.SiteSettingDao;
import org.seamoo.persistence.daos.jdo.JdoSiteSettingDaoImpl;
import org.seamoo.persistence.daos.jdo.PMF;
import org.seamoo.persistence.test.LocalDatastoreTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class JdoSiteSettingDAOImplTest extends LocalDatastoreTest {
	SiteSettingDao siteSettingDAO;

	@Override
	@Before
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		siteSettingDAO = new JdoSiteSettingDaoImpl();
	}

	@Override
	@After
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
		assertNotNull(siteSettingDAO.findById("model"));
	}

	@Test
	public void assignExistentSettingShouldUpdateExistingEntity() {
		SiteSetting setting = new SiteSetting("model", "bootstrap");
		siteSettingDAO.persist(setting);
		siteSettingDAO.assignSetting("model", "normal");
		assertEquals("normal", siteSettingDAO.findById("model").getValue());
	}

	@Test
	public void assignExistentSettingShouldNotCreateNewEntity() {
		SiteSetting setting = new SiteSetting("model", "bootstrap");
		siteSettingDAO.persist(setting);
		siteSettingDAO.assignSetting("model", "normal");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(SiteSetting.class);
		query.setFilter("key==myKey");
		query.declareParameters("String myKey");
		List<SiteSetting> settings = (List<SiteSetting>) query.execute("model");
		assertEquals(1, settings.size());

	}

}
