package com.psvgs.dal;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    public void testCreateUpdateDelete() {
        
        User user = ImmutableUser.builder().username("Iron Man").build();
        
        User clone = userDAO.create(user);
        
        assertEquals(clone, userDAO.findById(clone.getId()).orElseThrow());
        assertEquals(clone, userDAO.findByUsername(clone.getUsername()).orElseThrow());
        
        userDAO.update(ImmutableUser.builder().from(user).username("Wolverine").build());

        assertEquals(clone, userDAO.findById(clone.getId()).orElseThrow());

        userDAO.deleteById(clone.getId());
        
        assertEquals(Optional.empty(), userDAO.findById(clone.getId()));

    }
    
    @Test
    public void testFindByIdReturnsEmptyOptional() {
        assertEquals(Optional.empty(), userDAO.findById(UUID.randomUUID().toString()));
    }
    
}
