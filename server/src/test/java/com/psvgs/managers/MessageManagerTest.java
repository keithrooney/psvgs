package com.psvgs.managers;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.psvgs.dal.DAO;
import com.psvgs.models.ImmutableMessage;
import com.psvgs.models.ImmutableUser;
import com.psvgs.models.Message;

@ExtendWith(MockitoExtension.class)
public class MessageManagerTest {

    @InjectMocks
    private MessageManager messageManager;

    @Mock
    private DAO<Message> messageDAO;

    @AfterEach
    public void afterEach() {
        Mockito.verifyNoMoreInteractions(messageDAO);
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
                .recipient(ImmutableUser.builder().id(UUID.randomUUID().toString())
                        .username(UUID.randomUUID().toString()).build())
                .body("This is the body of the message!")
                .build();
        messageManager.create(message);
        Mockito.verify(messageDAO).create(message);
    }

    @Test
    public void testCreateThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> messageManager.create(null));
    }

    @Test
    public void testUpdate() {
        Message message = ImmutableMessage.builder()
                .recipient(ImmutableUser.builder().id(UUID.randomUUID().toString())
                        .username(UUID.randomUUID().toString()).build())
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

}
