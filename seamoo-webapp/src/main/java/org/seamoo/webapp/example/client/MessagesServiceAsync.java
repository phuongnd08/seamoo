package org.seamoo.webapp.example.client;

import org.seamoo.webapp.example.model.Messages;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface MessagesServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.seamoo.webapp.example.model.Messages
     */
    void create( org.seamoo.webapp.example.model.Message p0, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.seamoo.webapp.example.model.Messages
     */
    void getAll( AsyncCallback<java.util.Collection> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see org.seamoo.webapp.example.model.Messages
     */
    void deleteById( java.lang.Long p0, AsyncCallback<Void> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static MessagesServiceAsync instance;

        public static final MessagesServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (MessagesServiceAsync) GWT.create( MessagesService.class );
                ServiceDefTarget target = (ServiceDefTarget) instance;
                target.setServiceEntryPoint( GWT.getModuleBaseURL() + "messages" );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instanciated
        }
    }
}
