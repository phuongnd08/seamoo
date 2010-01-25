package org.seamoo.server;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seamoo.GuestBookEntry;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ViewGuestBook extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp);
	}

	protected void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (req.getMethod().equalsIgnoreCase("post")) {
			String username = req.getParameter("username");
			String message = req.getParameter("message");
			GuestBookEntry entry = new GuestBookEntry(username, message);
			PersistenceManager manager = PMF.get().getPersistenceManager();
			manager.makePersistent(entry);
			manager.close();
		}
		PersistenceManager manager = PMF.get().getPersistenceManager();
		Query query = manager.newQuery("select from "
				+ GuestBookEntry.class.getName());
		List<GuestBookEntry> entries = (List<GuestBookEntry>) query.execute();
		// manager.close();
		req.setAttribute("entries", entries);
		req.getRequestDispatcher("guestbook.jsp").forward(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		process(req, resp);
	}
}
