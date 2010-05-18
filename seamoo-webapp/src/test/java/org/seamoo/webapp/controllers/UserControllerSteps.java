package org.seamoo.webapp.controllers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.seamoo.daos.MemberDao;
import org.seamoo.entities.Member;
import org.springframework.web.servlet.ModelAndView;

import com.dyuproject.openid.OpenIdUser;
import com.dyuproject.openid.RelyingParty;

public class UserControllerSteps {
	OpenIdUser user;

	MemberDao memberDao;
	Member savedMember;

	@Given("A MemberDao")
	public void initMemberDao() {
		memberDao = mock(MemberDao.class);
		resetMemberDao();
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
	}

	RelyingParty relyingParty;

	@Given("An RelyingParty")
	public void initRelyingParty() {
		relyingParty = mock(RelyingParty.class);
		PowerMockito.mockStatic(RelyingParty.class);
		when(RelyingParty.getInstance()).thenReturn(relyingParty);
	}

	@Given("An $authenticacity OpenIdUser")
	public void initOpenIdUser(String authenticacity) throws Exception {
		user = mock(OpenIdUser.class);
		when(user.isAuthenticated()).thenReturn(authenticacity.equals("authenticated"));
		when(relyingParty.discover((HttpServletRequest) any())).thenReturn(user);
	}

	@Given("A null OpenIdUser")
	public void initNullOpenIdUser() throws Exception {
		user = null;
		when(relyingParty.discover((HttpServletRequest) any())).thenReturn(user);
	}

	HttpServletRequest request;

	@Given("An HttpServletRequest")
	public void initHttpServletRequest() {
		request = mock(HttpServletRequest.class);
		when(request.getContextPath()).thenReturn("");
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

	@When("User is logging in")
	public void login() throws Exception {
		mav = controller.login(request, returnUrl);
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
		mav = controller.welcomeFirstTime(request, mock(HttpServletResponse.class), returnUrl, command);
	}

	@Then("RelyingParty is invalidated")
	public void assertRelyingPartyBeingInvalidated() throws IOException {
		verify(relyingParty).invalidate((HttpServletRequest) any(), (HttpServletResponse) any());
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
}
