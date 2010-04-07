package org.seamoo.webapp.example.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.seamoo.persistence.daos.jdo.PMF;
import org.seamoo.webapp.example.model.Message;
import org.seamoo.webapp.example.model.Messages;

public class MessageRepository implements Messages {

	PersistenceManagerFactory pmfInstance = PMF.get();

	/**
     * @see net.kindleit.gae.example.model.Messages#getAll()
     */
	public Collection<Message> getAll() {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		try {
			List<Message> messages = new ArrayList<Message>();
		    Extent<Message> extent = pm.getExtent(Message.class, false);
		    for (Message message : extent) {
		        messages.add(message);
		    }
		    extent.closeAll();
			
		    return messages;
		} finally {
			pm.close();
		}
	}

	/**
     * @see net.kindleit.gae.example.model.Messages#create(net.kindleit.gae.example.model.Message)
     */
	public void create(Message message) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		try {
		    pm.makePersistent(message);
		} finally {
			pm.close();
		}
	}

	/**
     * @see net.kindleit.gae.example.model.Messages#deleteById(java.lang.Long)
     */
	public void deleteById(Long id) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		try {
			pm.deletePersistent(pm.getObjectById(Message.class, id));
		} finally {
			pm.close();
		}
	}
}
