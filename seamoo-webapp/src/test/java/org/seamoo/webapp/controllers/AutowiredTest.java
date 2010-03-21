package org.seamoo.webapp.controllers;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seamoo.persistence.SiteSettingDAO;
import org.seamoo.persistence.SubjectDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:dao-context.xml")
public class AutowiredTest {
	
	@Autowired
	SubjectDAO subjectDAO;
	@Autowired
	SiteSettingDAO siteSettingDAO;

	@Test
	public void subjectDAOShouldBeAutowired() {
		assertNotNull(subjectDAO);
	}

	@Test
	public void siteSettingDAOShouldBeAutowired() {
		assertNotNull(siteSettingDAO);
	}

}
