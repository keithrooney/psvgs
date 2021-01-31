package com.psvgs.dal;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

import com.psvgs.models.Emoji;
import com.psvgs.models.ImmutableLike;
import com.psvgs.models.ImmutableMessage;
import com.psvgs.models.ImmutableMessageQuery;
import com.psvgs.models.Like;
import com.psvgs.models.Message;
import com.psvgs.models.MessageQuery;

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

        Like like = ImmutableLike.builder().username(UUID.randomUUID().toString()).emoji(Emoji.THUMBS_UP).build();
        messageDAO.like(clone.getId(), like);

        clone = messageDAO.findById(clone.getId()).orElseThrow();
        assertEquals(Collections.singletonList(like), clone.getLikes());
        
        messageDAO.deleteById(clone.getId());

        assertEquals(Optional.empty(), messageDAO.findById(clone.getId()));

    }
    
    @Test
    public void testFindByIdReturnsEmptyOptional() {
        assertEquals(Optional.empty(), messageDAO.findById(UUID.randomUUID().toString()));
    }
    
    @Test
    public void testQuery() {

        List<Message> messages = Arrays.asList(
            ImmutableMessage.builder().sender("me").recipient("you").body("Pub!").build(),
            ImmutableMessage.builder().sender("you").recipient("me").body("Yes!!!").build(),
            ImmutableMessage.builder().sender("him").recipient("her").body("Hey!").build(),
            ImmutableMessage.builder().sender("him").recipient("her").body("You there?").build()
        );
        
        for (Message message: messages) {
            messageDAO.create(message);
        }
        
        MessageQuery query = ImmutableMessageQuery.builder().participants(new HashSet<>(Arrays.asList("me", "you"))).build();
        assertEquals(2, messageDAO.query(query).size());

        query = ImmutableMessageQuery.builder().participants(new HashSet<>(Arrays.asList("him"))).build();
        assertEquals(2, messageDAO.query(query).size());

        query = ImmutableMessageQuery.builder().participants(new HashSet<>(Arrays.asList("him"))).pageSize(1).build();
        assertEquals(1, messageDAO.query(query).size());

        query = ImmutableMessageQuery.builder().participants(new HashSet<>(Arrays.asList("him"))).page(3).pageSize(1).build();
        assertEquals(0, messageDAO.query(query).size());

        query = ImmutableMessageQuery.builder().participants(new HashSet<>(Arrays.asList("him"))).page(-1).pageSize(-1).build();
        assertEquals(2, messageDAO.query(query).size());

    }
    
    public static class ContainerSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.data.mongodb.host=" + CONTAINER.getHost(),
                    "spring.data.mongodb.port=" + CONTAINER.getMappedPort(27017),
                    "spring.data.mongodb.database=psvgs"
            );
        }
    }
}
