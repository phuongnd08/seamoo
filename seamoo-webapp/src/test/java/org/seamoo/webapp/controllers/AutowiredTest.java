package org.seamoo.webapp.controllers;

import static org.testng.Assert.*;

import org.seamoo.persistence.daos.LeagueDao;
import org.seamoo.persistence.daos.MemberDao;
import org.seamoo.persistence.daos.SiteSettingDao;
import org.seamoo.persistence.daos.SubjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "classpath:dao-context.xml")
public class AutowiredTest extends AbstractTestNGSpringContextTests {

	@Autowired
	SubjectDao subjectDao;
	@Autowired
	SiteSettingDao siteSettingDao;
	@Autowired
	LeagueDao leagueDao;
	@Autowired
	MemberDao memberDao;

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
}
