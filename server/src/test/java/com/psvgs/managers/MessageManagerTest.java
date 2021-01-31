package com.psvgs.managers;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.psvgs.dal.MessageDAO;
import com.psvgs.dal.UserDAO;
import com.psvgs.models.ImmutableMessage;
import com.psvgs.models.Message;
import com.psvgs.models.MessageQuery;
import com.psvgs.models.User;

@ExtendWith(MockitoExtension.class)
public class MessageManagerTest {

    @InjectMocks
    private MessageManager messageManager;

    @Mock
    private MessageDAO messageDAO;
    
    @Mock
    private UserDAO userDAO;

    @AfterEach
    public void afterEach() {
        Mockito.verifyNoMoreInteractions(messageDAO, userDAO);
    }

    @Test
    public void testFindById() {
        String id = UUID.randomUUID().toString();
        messageManager.findById(id);
        Mockito.verify(messageDAO).findById(id);
    }

    @Test
    public void testCreate() {
        Message message = ImmutableMessage.builder()
                .sender(UUID.randomUUID().toString())
                .recipient(UUID.randomUUID().toString())
                .body("This is the body of the message!")
                .build();
        
        User user = Mockito.mock(User.class);
        Mockito.when(userDAO.findByUsername(message.getSender())).thenReturn(Optional.of(user));
        Mockito.when(userDAO.findByUsername(message.getRecipient())).thenReturn(Optional.of(user));
        
        messageManager.create(message);
        
        Mockito.verify(messageDAO).create(message);
        Mockito.verify(userDAO).findByUsername(message.getSender());
        Mockito.verify(userDAO).findByUsername(message.getRecipient());
        
    }

    @Test
    public void testCreateThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> messageManager.create(null));
    }

    @Test
    public void testUpdate() {
        Message message = ImmutableMessage.builder()
                .sender(UUID.randomUUID().toString())
                .recipient(UUID.randomUUID().toString())
                .body("This is the body of the message!")
                .build();
        messageManager.update(message);
        Mockito.verify(messageDAO).update(message);
    }

    @Test
    public void testUpdateThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> messageManager.update(null));
    }

    @Test
    public void testDeleteById() {
        String id = UUID.randomUUID().toString();
        messageManager.deleteById(id);
        Mockito.verify(messageDAO).deleteById(id);
    }

    @Test
    public void testDeleteByIdThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> messageManager.deleteById(null));
    }
    
    @Test
    public void testQuery() {
        MessageQuery query = Mockito.mock(MessageQuery.class);
        messageManager.query(query);
        Mockito.verify(messageDAO).query(query);
    }

}
