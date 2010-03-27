package org.seamoo.webapp.example.client;

import java.util.List;

import org.seamoo.webapp.example.model.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MessagesServiceAsync {

	void getAll(AsyncCallback<List<Message>> callback);

	void create(Message message, AsyncCallback<Void> callback);

	void deleteById(Long id, AsyncCallback<Void> callback);

}
