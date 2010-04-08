package org.seamoo.webapp.client.admin;

import static org.testng.Assert.*;

import java.util.List;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.seamoo.entities.Subject;
import org.seamoo.webapp.integration.shared.TestConfig;

import com.gdevelop.gwt.syncrpc.SyncProxy;

public class SubjectServiceAsyncSteps {

	private SubjectService subjectService;

	@Given("A remote SubjectService")
	public void initRemoteSubjectService() {
		subjectService = (SubjectService) SyncProxy.newProxyInstance(SubjectService.class, TestConfig.getServerBase()
				+ "/adminSubjects/", "../services/subject");
	}

	List<Subject> listOfSubjects;

	@When("All Subject are retrieved")
	public void getAllSubjects() {
		listOfSubjects = subjectService.getAll();
	}

	@Then("The number of Subjects is at least $min")
	public void assertMinimumNumberOfSubjects(int min) {
		assertTrue(listOfSubjects.size() > min, "listOfSubjects.size()<=" + min);
	}

	private Subject subject;

	@Given("A new Subject of name \"$name\"")
	public void initSubject(String name) {
		subject = new Subject();
		subject.setName(name);
	}

	@When("The Subject is persisted")
	public void persistSubject() {
		subject = subjectService.save(subject);
	}

	@Then("Subject is assigned an Id")
	public void assertIdGenerated() {
		assertTrue(subject.getAutoId() != 0L);

	}

	@When("Subject name is changed to \"$newName\"")
	public void changeSubjectName(String newName) {
		subject.setName(newName);
	}

	private Subject newSubject;

	@When("Edited Subject is persisted")
	public void persistEditedSubject() {
		newSubject = subjectService.save(subject);
	}

	@Then("Edited Subject Id is equal Subject Id")
	public void assertSubjectIdConsistency() {
		assertEquals(newSubject.getAutoId(), subject.getAutoId());
	}

	@When("findById is performed")
	public void findSubjectById() {
		subject = subjectService.load(subject.getAutoId().toString());
	}

	@Then("Subject name is \"$name\"")
	public void assertSubjectName(String name) {
		assertEquals(subject.getName(), name);
	}

	@When("delete is performed")
	public void deleteSubject() {
		subjectService.delete(subject);
	}

	@Then("A null Subject is returned")
	public void assertNullSubject() {
		assertEquals(subject, null);
	}
}
