package org.seamoo.webapp.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.seamoo.entities.Member;
import org.seamoo.persistence.daos.MemberDao;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.dyuproject.openid.OpenIdUser;
import com.dyuproject.openid.RelyingParty;

public class UserProfileInjectionFilter extends GenericFilterBean {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	public UserProfileInjectionFilter() {
		relyingParty = RelyingParty.getInstance();
	}

	RelyingParty relyingParty;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		// TODO Auto-generated method stub
		try {
			OpenIdUser openIdUser = relyingParty.discover((HttpServletRequest) request);
			System.out.println("Discover OpenIdUser " + openIdUser);
			if (openIdUser != null) {
				MemberDao memberDao = WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean(
						MemberDao.class);
				Member member = memberDao.findById(openIdUser.getClaimedId());
				if (member == null) {
					System.out.println("User never seen on system, create new member");
					member = new Member(openIdUser.getClaimedId());
					memberDao.persist(member);
				} else
					System.out.println("User seen on system: " + member.toString());
				request.setAttribute("member", member);// inject member
														// information into
														// context
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		chain.doFilter(request, response);

	}

}
