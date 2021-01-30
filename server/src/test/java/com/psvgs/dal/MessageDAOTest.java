package com.psvgs.dal;

import static org.junit.Assert.assertEquals;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.psvgs.models.ImmutableMessage;
import com.psvgs.models.Message;

@ActiveProfiles("test")
@Import(MessageDAO.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = MessageDAOTest.ContainerSourceInitializer.class)
@Testcontainers
public class MessageDAOTest {
    
    public static final MongoDBContainer CONTAINER = new MongoDBContainer("mongo:4.4.3");
    
    static {
        CONTAINER.start();
    }
    
    @Autowired
    private MessageDAO messageDAO;
    
    @Test
    public void testUpdate() {
    
        Message message = ImmutableMessage.builder().sender(UUID.randomUUID().toString())
                .recipient(UUID.randomUUID().toString()).body("This is the body of the message!").build();

        Message clone = messageDAO.create(message);

        assertEquals(clone, messageDAO.findById(clone.getId()).orElseThrow());

        clone = messageDAO.update(ImmutableMessage.builder().from(clone).body("This is the new body!").build());

        assertEquals(clone, messageDAO.findById(clone.getId()).orElseThrow());

        messageDAO.deleteById(clone.getId());

        assertEquals(Optional.empty(), messageDAO.findById(clone.getId()));

    }
    
    @Test
    public void testFindByIdReturnsEmptyOptional() {
        assertEquals(Optional.empty(), messageDAO.findById(UUID.randomUUID().toString()));
    }
    
    public static class ContainerSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.data.mongodb.host=" + CONTAINER.getHost(),
                    "spring.data.mongodb.port=27017",
                    "spring.data.mongodb.database=messages"
            );
        }
    }
}
