package org.seamoo.webapp.autowired;

import static org.testng.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.SiteSettingDao;
import org.seamoo.daos.SubjectDao;
import org.seamoo.daos.question.QuestionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

@ContextConfiguration(locations = "classpath:dao-context.xml")
public class AutowiredTest extends AbstractTestNGSpringContextTests {

	LocalServiceTestHelper helper;

	public AutowiredTest() {
		LocalDatastoreServiceTestConfig testConfig = new LocalDatastoreServiceTestConfig();
		helper = new LocalServiceTestHelper(testConfig);
		helper.setUp();
	}

	@AfterClass
	public void tearDownEnv() {
		helper.tearDown();
	}

	@Autowired
	SubjectDao subjectDao;
	@Autowired
	SiteSettingDao siteSettingDao;
	@Autowired
	LeagueDao leagueDao;
	@Autowired
	MemberDao memberDao;
	@Autowired
	QuestionDao questionDao;

	@Test
	public void subjectDAOShouldBeAutowired() {
		assertNotNull(subjectDao);
	}

	@Test
	public void siteSettingDAOShouldBeAutowired() {
		assertNotNull(siteSettingDao);
	}

	@Test
	public void leagueDAOShouldBeAutowired() {
		assertNotNull(leagueDao);
	}

	@Test
	public void memberDAOShouldBeAutowired() {
		assertNotNull(memberDao);
	}

	@Test
	public void questionDAOShouldBeAutowired() {
		assertNotNull(questionDao);
	}

}
