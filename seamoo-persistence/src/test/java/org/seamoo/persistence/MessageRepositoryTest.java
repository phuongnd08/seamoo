package org.seamoo.persistence;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.seamoo.webapp.example.model.Message;
import org.seamoo.webapp.example.server.MessageRepository;

/**
 * Tests {@link MessageRepository} class.
 * 
 * @author androns
 */
public class MessageRepositoryTest extends LocalDatastoreTest {

    private MessageRepository messageRepository;

    /**
     * @see LocalDatastoreTest#setUp()
     */
    @Before
    @Override
    public void setUp() {
        super.setUp();
        messageRepository = new MessageRepository();
    }

    /**
     * @see LocalDatastoreTest#tearDown()
     */
    @After
    @Override
    public void tearDown() {
        super.tearDown();
    }

    /**
     * 
     */
    @Test
    public void smokeTest() {
        assertNotNull(messageRepository);

        // create
        Message message = new Message();
        message.setText("Test message");

        messageRepository.create(message);

        // read
        Collection<Message> messages = messageRepository.getAll();

        assertNotNull(messages);
        assertEquals(1, messages.size());
        Message storedMessage = messages.iterator().next();

        assertNotNull(storedMessage.getId());
        assertEquals(message.getText(), storedMessage.getText());

        // delete
        messageRepository.deleteById(storedMessage.getId());

        messages = messageRepository.getAll();
        assertEquals(0, messages.size());
    }
}
