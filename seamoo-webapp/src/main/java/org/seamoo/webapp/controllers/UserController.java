package org.seamoo.webapp.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seamoo.entities.Member;
import org.seamoo.persistence.daos.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dyuproject.openid.Constants;
import com.dyuproject.openid.OpenIdUser;
import com.dyuproject.openid.RelyingParty;
import com.dyuproject.openid.ext.AxSchemaExtension;
import com.dyuproject.openid.ext.SRegExtension;
import com.dyuproject.util.http.UrlEncodedParameterMap;

@Controller
@RequestMapping("/users")
public class UserController {
	@Autowired
	MemberDao memberDao;
	public static final String OPEN_ID_INFO_FIELD = "info";
	public static final String OPEN_ID_INFO_EMAIL_FIELD = "email";
	public static final String OPEN_ID_INFO_COUNTRY_FIELD = "country";
	public static final String OPEN_ID_INFO_LANGUAGE_FIELD = "language";
	static {
		RelyingParty.getInstance().addListener(
				new AxSchemaExtension().addExchange(OPEN_ID_INFO_EMAIL_FIELD).addExchange(OPEN_ID_INFO_COUNTRY_FIELD).addExchange(
						OPEN_ID_INFO_LANGUAGE_FIELD)).addListener(
				new SRegExtension().addExchange(OPEN_ID_INFO_EMAIL_FIELD).addExchange(OPEN_ID_INFO_COUNTRY_FIELD).addExchange(
						OPEN_ID_INFO_LANGUAGE_FIELD)).addListener(new RelyingParty.Listener() {
			public void onDiscovery(OpenIdUser user, HttpServletRequest request) {

			}

			public void onPreAuthenticate(OpenIdUser user, HttpServletRequest request, UrlEncodedParameterMap params) {
				if ("true".equals(request.getParameter("popup"))) {
					String returnTo = params.get(Constants.OPENID_TRUST_ROOT) + request.getContextPath() + "/popup_verify.html";
					params.put(Constants.OPENID_RETURN_TO, returnTo);
					params.put(Constants.OPENID_REALM, returnTo);
					params.put("openid.ns.ui", "http://specs.openid.net/extensions/ui/1.0");
					params.put("openid.ui.mode", "popup");
				}
			}

			public void onAuthenticate(OpenIdUser user, HttpServletRequest request) {
				Map<String, String> sreg = SRegExtension.remove(user);
				Map<String, String> axschema = AxSchemaExtension.remove(user);
				if (sreg != null && !sreg.isEmpty()) {
					System.err.println("sreg: " + sreg);
					user.setAttribute(OPEN_ID_INFO_EMAIL_FIELD, sreg);
				} else if (axschema != null && !axschema.isEmpty()) {
					System.err.println("axschema: " + axschema);
					user.setAttribute(OPEN_ID_INFO_FIELD, axschema);
				} else {
					System.err.println("identity: " + user.getIdentity());
				}
			}

			public void onAccess(OpenIdUser user, HttpServletRequest request) {

			}
		});
	}

	private static String getRedirectView(HttpServletRequest request, String url) {
		return String.format("redirect:%s", url != null && !url.equals("") ? url : request.getContextPath() + "/");
	}

	@RequestMapping(value = "/first-seen", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView welcomeFirstTime(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "returnUrl", required = false) String returnUrl,
			@RequestParam(value = "cmd", required = false) String cmd) throws Exception {
		OpenIdUser user = RelyingParty.getInstance().discover(request);
		Member member = memberDao.findById(user.getClaimedId());
		if (member != null) {
			return new ModelAndView(getRedirectView(request, returnUrl));
		}

		if (request.getMethod().equalsIgnoreCase("post")) {
			if ("cancel".equals(cmd) || "create".equals(cmd)) {
				if (cmd.equals("cancel"))
					RelyingParty.getInstance().invalidate(request, response);
				else /* cmd.equals("create") */{
					member = new Member();
					member.setOpenId(user.getClaimedId());
					member.setJoiningDate(new Date());
					String email = null;
					Map<String, String> infoMap = (Map<String, String>) user.getAttribute(OPEN_ID_INFO_FIELD);
					if (infoMap != null) {
						member.setEmail(infoMap.get(OPEN_ID_INFO_EMAIL_FIELD));
					}
					memberDao.persist(member);
				}

				return new ModelAndView(getRedirectView(request, returnUrl));
			}
		}
		ModelAndView mav = new ModelAndView("users.first-time");
		mav.addObject("title", "Xác nhận Open ID");
		return mav;
	}

	@RequestMapping(value = { "/", "" })
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView("users.list");
		mav.addObject("title", "Users");
		return mav;
	}

	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, @RequestParam(value = "returnUrl", required = false) String returnUrl)
			throws Exception {
		OpenIdUser user = RelyingParty.getInstance().discover(request);
		if (user != null && user.isAuthenticated()) {
			return new ModelAndView(getRedirectView(request, returnUrl));
		} else {
			ModelAndView mav = new ModelAndView("users.login");
			mav.addObject("title", "Đăng nhập với Open ID của bạn");
			mav.addObject("returnUrl", returnUrl);
			return mav;
		}
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "returnUrl", required = false) String returnUrl) throws IOException {
		RelyingParty.getInstance().invalidate(request, response);
		if (returnUrl != null && !returnUrl.trim().equals(""))
			return String.format("redirect:%s", returnUrl);
		else
			return String.format("redirect:%s", "/");
	}

	// @RequestMapping("/{userId}/{userName}")
	// public ModelAndView viewProfile(@PathVariable("userId") long userId,
	// @PathVariable("userName") String userName){
	// return viewProfileTab(userId, userName, "profile");
	// }
	@RequestMapping("/{userId}/{userName}")
	public ModelAndView viewProfileTab(@PathVariable("userId") long userId, @PathVariable("userName") String userName,
			@RequestParam(value = "tab", required = false) String tab) {
		ModelAndView mav = null;
		if (tab != null && tab.equals("discussion")) {
			mav = new ModelAndView("user.discussion");
			mav.addObject("title", "Discussion");
		} else {
			mav = new ModelAndView("user.view");
			mav.addObject("userName", userName);
			mav.addObject("userId", new Long(userId));
			mav.addObject("title", userName);
		}
		return mav;
	}

	@RequestMapping("/edit/{userId}")
	public ModelAndView edit(@PathVariable("userId") long userId) {
		ModelAndView mav = new ModelAndView("user.edit");
		mav.addObject("title", String.format("Edit profile %d", userId));
		return mav;
	}
}
