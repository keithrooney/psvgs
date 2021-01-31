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
import com.psvgs.models.ImmutableUser;
import com.psvgs.models.User;

@ExtendWith(MockitoExtension.class)
public class UserManagerTest {

    @InjectMocks
    private UserManager userManager;

    @Mock
    private DAO<User> userDAO;

    @AfterEach
    public void afterEach() {
        Mockito.verifyNoMoreInteractions(userDAO);
    }

    @Test
    public void testFindById() {
        String id = UUID.randomUUID().toString();
        userManager.findById(id);
        Mockito.verify(userDAO).findById(id);
    }

    @Test
    public void testFindByIdThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> userManager.findById(null));
    }

    @Test
    public void testCreate() {
        User user = ImmutableUser.builder().username(UUID.randomUUID().toString()).build();
        userManager.create(user);
        Mockito.verify(userDAO).create(user);
    }

    @Test
    public void testCreateThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> userManager.create(null));
    }
    
    @Test
    public void testUpdate() {
        User user = ImmutableUser.builder().username(UUID.randomUUID().toString()).build();
        userManager.update(user);
        Mockito.verify(userDAO).update(user);
    }

    @Test
    public void testUpdateThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> userManager.update(null));
    }

    @Test
    public void testDeleteById() {
        String id = UUID.randomUUID().toString();
        userManager.deleteById(id);
        Mockito.verify(userDAO).deleteById(id);
    }

    @Test
    public void testDeleteByIdThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> userManager.deleteById(null));
    }

}
