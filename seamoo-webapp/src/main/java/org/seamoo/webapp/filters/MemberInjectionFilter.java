package org.seamoo.webapp.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seamoo.entities.Member;
import org.seamoo.persistence.daos.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;

import com.dyuproject.openid.OpenIdUser;
import com.dyuproject.openid.RelyingParty;

public class MemberInjectionFilter implements Filter {

	public static final String MEMBER_FIELD = "member";
	@Autowired
	MemberDao memberDao;
	String firstSeenUri;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		// TODO Auto-generated method stub
		try {
			OpenIdUser openIdUser = RelyingParty.getInstance().discover((HttpServletRequest) request);
			System.out.println("Discover OpenIdUser " + openIdUser);
			if (openIdUser != null && openIdUser.isAuthenticated()) {
				Member member = memberDao.findById(openIdUser.getClaimedId());
				if (member == null) {
					System.out.println("User never seen on system, route to first-seen");
					String requestUri = ((HttpServletRequest) request).getRequestURI();
					if (!requestUri.startsWith(firstSeenUri)) {
						((HttpServletResponse) response).sendRedirect(String.format("%s?returnUrl=%s", firstSeenUri, requestUri));
						return;
					}
				} else
					System.out.println("User seen on system: " + member.toString());
				request.setAttribute(MEMBER_FIELD, member);// inject member
				// information into
				// context
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		firstSeenUri = config.getInitParameter("firstSeenUri");
		if (firstSeenUri == null) {
			throw new ServletException("firstSeenUri cannot be null");
		}
	}
}
