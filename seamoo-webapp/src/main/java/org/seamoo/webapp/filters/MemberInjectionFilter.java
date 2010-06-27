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

import org.openid4java.discovery.Identifier;
import org.seamoo.daos.MemberDao;
import org.seamoo.entities.Member;
import org.seamoo.utils.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class MemberInjectionFilter implements Filter {

	public static final String MEMBER_FIELD = "member";
	@Autowired
	MemberDao memberDao;
	String firstSeenUri;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	public static Member getInjectedMember(HttpServletRequest request) {
		return (Member) request.getAttribute(MEMBER_FIELD);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		try {
			Identifier identifier = (Identifier) httpRequest.getSession().getAttribute("identifier");
			System.out.println("Discover identifier" + identifier);
			if (identifier != null) {
				Member member = memberDao.findByOpenId(identifier.getIdentifier());
				if (member == null) {
					String requestUri = ((HttpServletRequest) request).getRequestURI();
					if (!requestUri.startsWith(firstSeenUri)) {
						((HttpServletResponse) response).sendRedirect(String.format("%s?returnUrl=%s", firstSeenUri,
								UrlBuilder.getEncodedUrl(httpRequest.getRequestURI(), httpRequest.getQueryString())));
						return;
					}
				}
				request.setAttribute(MEMBER_FIELD, member);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		firstSeenUri = config.getInitParameter("firstSeenUri");
		if (firstSeenUri == null) {
			throw new ServletException("firstSeenUri cannot be null");
		}
	}
}
