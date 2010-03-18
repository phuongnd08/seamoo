package org.seamoo.webapp.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seamoo.persistence.SubjectDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:persistence-api.xml")
public class AutowiredTest {
	@Autowired
	SubjectDAO subjectDAO;

	@Test
	public void subjectDAOWired() {
		assertNotNull(subjectDAO);
		assertTrue(subjectDAO instanceof org.seamoo.persistence.jdo.JdoSubjectDAOImpl);
	}
}
