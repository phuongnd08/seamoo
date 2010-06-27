package org.seamoo.webapp.controllers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openid4java.discovery.Identifier;
import org.seamoo.daos.MemberDao;
import org.seamoo.entities.Member;
import org.seamoo.webapp.OpenIdConsumer;
import org.springframework.web.servlet.ModelAndView;

public class UserControllerSteps {
	Identifier identifier;
	OpenIdConsumer openIdConsumer;

	MemberDao memberDao;
	Member savedMember;

	@Given("A MemberDao")
	public void initMemberDao() {
		memberDao = mock(MemberDao.class);
		resetMemberDao();
	}

	@Given("An OpenIdConsumer")
	public void initOpenIdConsumer() {
		openIdConsumer = mock(OpenIdConsumer.class);
	}

	@Given("MemberDao is reset")
	public void resetMemberDao() {
		reset(memberDao);
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				savedMember = (Member) invocation.getArguments()[0];
				return null;
			}

		}).when(memberDao).persist((Member) any());
	}

	@Given("$number member exists")
	public void initNumberOfMember(long number) {
		when(memberDao.countAll()).thenReturn(number);
	}

	UserController controller;

	@Given("A UserController")
	public void initUserController() {
		controller = new UserController();
		controller.memberDao = memberDao;
		controller.openIdConsumer = openIdConsumer;
	}

	@Given("An Identifier")
	public void initIdentifier() throws Exception {
		Identifier identifier = mock(Identifier.class);
		when(openIdConsumer.verifyResponse((HttpServletRequest) any())).thenReturn(identifier);
	}

	@Given("An empty Identifier")
	public void initNullOpenIdUser() throws Exception {
		identifier = null;
		when(openIdConsumer.verifyResponse((HttpServletRequest) any())).thenReturn(identifier);
	}

	HttpServletRequest request;
	HttpSession session;
	HttpServletResponse response;

	@Given("An HttpServletRequest")
	public void initHttpServletRequest() {
		request = mock(HttpServletRequest.class);
		session = mock(HttpSession.class);
		when(request.getContextPath()).thenReturn("");
		when(request.getSession()).thenReturn(session);
	}

	@Given("An HttpServletResponse")
	public void initHttpServletResponse() {
		response = mock(HttpServletResponse.class);
	}

	@Given("Session forgot its interaction")
	public void resetSession() {
		reset(session);
	}

	String returnUrl;

	@Given("returnUrl is null")
	public void setupNullReturnUrl() {
		returnUrl = null;
	}

	@Given("returnUrl is \"$returnUrl\"")
	public void setupCustomReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	ModelAndView mav;

	boolean isReturned = false;

	@Given("A Returned login transaction")
	public void setUpLoginReturn() {
		isReturned = true;
	}

	@Given("No Returned login transaction")
	public void setUpNoLoginReturn() {
		isReturned = false;
	}

	@Given("email is \"$email\"")
	public void setUpEmail(String email) {
		when(request.getAttribute("email")).thenReturn(email);
	}

	@Given("email is null")
	public void setUpNullEmail() {
		when(request.getAttribute("email")).thenReturn(null);
	}

	@When("User is logging in")
	public void login() throws Exception {
		mav = controller.login(request, response, isReturned, returnUrl, null);
	}

	@Then("User is being routed to \"$view\"")
	public void assertView(String view) {
		assertEquals(mav.getViewName(), view);
	}

	@Then("Key \"$key\" with value \"$value\" is passed to view")
	public void assertKeyValuePassedToView(String key, String value) {
		Map<String, Object> map = mav.getModel();
		assertEquals(map.get(key), value);
	}

	@Given("Method is \"$method\"")
	public void setUpRequestMethod(String method) {
		when(request.getMethod()).thenReturn(method);
	}

	@Given("Member is $status the System")
	public void setUpMemberStatus(String status) {
		if (status.equals("on")) {
			when(memberDao.findByOpenId(anyString())).thenReturn(new Member());
		} else if (status.equals("not on")) {
			when(memberDao.findByOpenId(anyString())).thenReturn(null);
		} else
			throw new RuntimeException(String.format("Status \"%s\" is not supported", status));
	}

	String command = null;

	@Given("Command is \"$command\"")
	public void setUpCommand(String command) {
		this.command = command;
	}

	@When("User is being seen first time")
	public void welcomeFirstTime() throws Exception {
		mav = controller.welcomeFirstTime(request, response, returnUrl, null, command);
	}

	@Then("Identifier is assigned to session")
	public void assertIdentifierAssignedToSession() {
		verify(session).setAttribute(eq("identifier"), any());
	}

	@Then("Session is invalidated")
	public void assertRelyingPartyBeingInvalidated() throws IOException {
		verify(session).invalidate();
	}

	@Then("Member is created")
	public void assertMemberBeingCreated() {
		verify(memberDao).persist((Member) any());
	}

	@Then("Member is not administrator")
	public void assertSavedMemberIsNotAdministrator() {
		assertFalse(savedMember.isAdministrator());
	}

	@Then("Member is administrator")
	public void assertSavedMemberIsAdministrator() {
		assertTrue(savedMember.isAdministrator());
	}
	
	@Given("A Session with assigned Identifier")
	public void setUpSessionWithIdentifier(){
		Identifier identifier = mock(Identifier.class);
		when(session.getAttribute("identifier")).thenReturn(identifier);
	}
}
