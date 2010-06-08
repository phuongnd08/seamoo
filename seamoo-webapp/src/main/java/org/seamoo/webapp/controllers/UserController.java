package org.seamoo.webapp.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seamoo.daos.LeagueDao;
import org.seamoo.daos.LeagueMembershipDao;
import org.seamoo.daos.MemberDao;
import org.seamoo.daos.SubjectDao;
import org.seamoo.daos.matching.MatchDao;
import org.seamoo.entities.League;
import org.seamoo.entities.LeagueMembership;
import org.seamoo.entities.Member;
import org.seamoo.entities.matching.Match;
import org.seamoo.entities.matching.MatchCompetitor;
import org.seamoo.utils.AliasBuilder;
import org.seamoo.utils.EmailExtractor;
import org.seamoo.utils.HashBuilder;
import org.seamoo.utils.MapBuilder;
import org.seamoo.utils.converter.Converter;
import org.seamoo.webapp.Pager;
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
	@Autowired
	LeagueDao leagueDao;
	@Autowired
	SubjectDao subjectDao;
	@Autowired
	MatchDao matchDao;
	@Autowired
	LeagueMembershipDao leagueMembershipDao;
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
		String workingUrl;
		try {
			workingUrl = url != null && !url.equals("") ? URLDecoder.decode(url, "utf-8") : request.getContextPath() + "/";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			workingUrl = request.getContextPath() + "/";
		}
		return String.format("redirect:%s", workingUrl);
	}

	@RequestMapping(value = "/first-seen", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView welcomeFirstTime(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "returnUrl", required = false) String returnUrl,
			@RequestParam(value = "cmd", required = false) String cmd) throws Exception {
		OpenIdUser user = RelyingParty.getInstance().discover(request);
		Member member = memberDao.findByOpenId(user.getClaimedId());
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
					Map<String, String> infoMap = (Map<String, String>) user.getAttribute(OPEN_ID_INFO_FIELD);
					if (infoMap != null) {
						String email = infoMap.get(OPEN_ID_INFO_EMAIL_FIELD);
						member.setEmail(email);
						if (email != null) {
							member.setEmailHash(HashBuilder.getMD5Hash(email));
							member.setDisplayName(EmailExtractor.extractName(email));
						}
					}
					if (member.getDisplayName() != null)
						member.setAlias(AliasBuilder.toAlias(member.getDisplayName()));
					member.setAdministrator(memberDao.countAll() == 0);
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

	static final int MATCH_PER_PAGE = 10;
	static final int MEMBERSHIP_PER_PAGE = 10;

	@RequestMapping("/{memberAutoId}/{memberAlias}")
	public ModelAndView viewProfileTab(@PathVariable("memberAutoId") long memberAutoId,
			@PathVariable("memberAlias") String memberAlias, @RequestParam(value = "tab", required = false) String tab,
			@RequestParam(value = "m", required = false, defaultValue = "1") long matchPage,
			@RequestParam(value = "ms", required = false, defaultValue = "1") long membershipPage) {
		ModelAndView mav = null;
		if (tab != null && tab.equals("discussion")) {
			mav = new ModelAndView("user.discussion");
			mav.addObject("title", "Discussion");
		} else {
			mav = new ModelAndView("user.view");
			Member m = memberDao.findByKey(memberAutoId);
			mav.addObject("user", m);
			mav.addObject("title", "View Profile - " + m.getDisplayName());
			long matchCount = matchDao.countByMember(memberAutoId);
			long msCount = leagueMembershipDao.countByMember(memberAutoId);
			long matchPageCount = Pager.getPageCount(matchCount, MATCH_PER_PAGE);
			long msPageCount = Pager.getPageCount(msCount, MEMBERSHIP_PER_PAGE);
			List<Match> matches = matchDao.getRecentMatchesBymember(memberAutoId,
					Pager.getFromForPage(matchPage, MATCH_PER_PAGE), MATCH_PER_PAGE);
			mav.addObject("matches", matches);
			mav.addObject("matchPage", matchPage);
			long membershipFromIndex = Pager.getFromForPage(membershipPage, MEMBERSHIP_PER_PAGE);

			Map<Long, Boolean> requiredLeagues = new HashMap<Long, Boolean>();

			List<LeagueMembership> memberships = leagueMembershipDao.getRecentByMember(memberAutoId, membershipFromIndex,
					MEMBERSHIP_PER_PAGE);
			for (LeagueMembership ms : memberships) {
				requiredLeagues.put(ms.getLeagueAutoId(), true);
			}
			Map<Long, League> leaguesMap = leagueDao.findAllByKeys(requiredLeagues.keySet());
			Map<Long, Boolean> requiredSubjects = new HashMap<Long, Boolean>();
			for (Long leagueId : leaguesMap.keySet())
				requiredSubjects.put(leaguesMap.get(leagueId).getSubjectAutoId(), true);

			Map<Long, Boolean> requiredMembers = new HashMap<Long, Boolean>();
			for (Match ma : matches) {
				for (MatchCompetitor c : ma.getCompetitors())
					requiredMembers.put(c.getMemberAutoId(), true);
			}

			mav.addObject("memberships", memberships);
			mav.addObject("membershipPage", membershipPage);
			mav.addObject("membershipPageCount", msPageCount);
			mav.addObject("matchPageCount", matchPageCount);
			mav.addObject("memberAutoId", memberAutoId);
			mav.addObject("leaguesMap", MapBuilder.toFreeMarkerMap(leaguesMap));
			mav.addObject("subjectsMap", MapBuilder.toFreeMarkerMap(subjectDao.findAllByKeys(requiredSubjects.keySet())));
			mav.addObject("membersMap", MapBuilder.toFreeMarkerMap(memberDao.findAllByKeys(requiredMembers.keySet())));
			mav.addObject("membershipFromIndex", membershipFromIndex);
		}
		return mav;
	}

	public static String BIRTH_DAY_FORMAT = "yyyy/MM/dd";

	@RequestMapping(value = "/edit/{userId}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable("userId") long userId) {
		ModelAndView mav = new ModelAndView("user.edit");
		Member m = memberDao.findByKey(userId);
		mav.addObject("title", String.format("Edit profile %s", m.getDisplayName()));
		mav.addObject("user", m);
		mav.addObject("birthdayString", m.getDateOfBirth() != null ? Converter.toString(m.getDateOfBirth(), BIRTH_DAY_FORMAT)
				: "");
		return mav;
	}

	@RequestMapping(value = "/edit/{userId}", method = RequestMethod.POST)
	public String save(@PathVariable("userId") long userId, @RequestParam("displayName") String displayName,
			@RequestParam("email") String email, @RequestParam("birthday") String birthday,
			@RequestParam("website") String website, @RequestParam("quote") String quote, @RequestParam("aboutMe") String aboutMe) {
		Member m = memberDao.findByKey(userId);
		m.setDisplayName(displayName);
		m.setAlias(AliasBuilder.toAlias(displayName));
		m.setEmail(email);
		m.setEmailHash(HashBuilder.getMD5Hash(email));
		m.setQuote(quote);
		Date d = null;
		try {
			d = Converter.toDate(birthday, BIRTH_DAY_FORMAT);
		} catch (ParseException e) {
		}
		m.setDateOfBirth(d);
		m.setWebsite(website);
		m.setAboutMe(aboutMe);
		memberDao.persist(m);
		return String.format("redirect:/users/%d/%s", m.getAutoId(), m.getAlias());
	}
}
