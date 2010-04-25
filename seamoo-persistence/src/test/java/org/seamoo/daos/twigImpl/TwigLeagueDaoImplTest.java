package org.seamoo.daos.twigImpl;

import static org.testng.Assert.*;

import java.util.List;

import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.SubjectDao;
import org.seamoo.entities.League;
import org.seamoo.entities.Subject;
import org.seamoo.persistence.test.LocalAppEngineTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TwigLeagueDaoImplTest extends LocalAppEngineTest {
	LeagueDao leagueDao;
	SubjectDao subjectDao;
	Subject english;
	League[] leagues;

	@Override
	@BeforeMethod
	public void setUp() {
		// TODO Auto-generated method stub
		super.setUp();
		leagueDao = new TwigLeagueDaoImpl();
		subjectDao = new TwigSubjectDaoImpl();
		english = new Subject();
		subjectDao.persist(english);
		leagues = new League[] { new League(), new League(), new League() };
		leagues[0].setName("English Amateur");
		leagues[0].setSubjectAutoId(english.getAutoId());
		leagues[1].setName("English League 2");
		leagues[1].setSubjectAutoId(english.getAutoId());
		leagues[1].setEnabled(true);
		leagues[2].setName("English League 1");
		leagueDao.persist(leagues);
	}

	@Override
	@AfterMethod
	public void tearDown() {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@Test
	public void getAllBySubjectShouldReturnedLeaguesBelongToSubject() {
		List<League> leaguesBySubject = leagueDao.getAllBySubjectId(english.getAutoId());
		assertEquals(leaguesBySubject.size(), 2);
		assertEqualsNoOrder(new String[] { leagues[0].getName(), leagues[1].getName() }, new String[] {
				leaguesBySubject.get(0).getName(), leaguesBySubject.get(1).getName() });
	}

	@Test
	public void getEnabledBySubjectShouldReturnedLeaguesBelongToSubjectAndAreEnabled() {
		List<League> leaguesBySubject = leagueDao.getEnabledBySubjectId(english.getAutoId());
		assertEquals(leaguesBySubject.size(), 1);
		assertEquals(leagues[1].getName(), leaguesBySubject.get(0).getName());
	}
}
