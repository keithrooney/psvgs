package com.psvgs.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.psvgs.models.ImmutableUser;
import com.psvgs.models.User;

@JdbcTest
@ActiveProfiles("test")
@Import(UserDAO.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;
    
    @Test
    public void testCreate() {
        User user = ImmutableUser.builder().username("Iron Man").build();
        User clone = userDAO.create(user);
        assertEquals(clone, userDAO.findById(clone.getId()).orElseThrow());
    }
    
    @Test
    public void testCreateReturnsEmptyOptional() {
        assertEquals(Optional.empty(), userDAO.findById(UUID.randomUUID().toString()));
    }
    
    @Test
    public void testUpdateThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> userDAO.update(null));
    }

    @Test
    public void testDeleteByIdThrowsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> userDAO.deleteById(null));
    }

}
