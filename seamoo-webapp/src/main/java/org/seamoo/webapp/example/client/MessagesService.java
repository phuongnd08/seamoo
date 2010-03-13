package org.seamoo.webapp.example.client;

import org.seamoo.webapp.example.model.Messages;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("messages")
public interface MessagesService extends RemoteService, Messages {
    // marker interface
}
