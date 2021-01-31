package com.psvgs;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.psvgs.models.Emoji;
import com.psvgs.requests.ImmutableLikeUpdateRequest;
import com.psvgs.requests.ImmutableMessageCreateRequest;
import com.psvgs.requests.ImmutableMessageUpdateRequest;
import com.psvgs.requests.ImmutableUserCreateRequest;
import com.psvgs.requests.LikeUpdateRequest;
import com.psvgs.requests.MessageCreateRequest;
import com.psvgs.requests.MessageUpdateRequest;

@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = MessageControllerIT.ContainerSourceInitializer.class)
public class MessageControllerIT {

    public static final MongoDBContainer CONTAINER = new MongoDBContainer("mongo:4.4.3");

    static {
        CONTAINER.start();
    }
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUpdateDelete() throws JsonProcessingException, Exception {
        
        mockMvc.perform(
                post("/v1/users")
                .content(objectMapper.writeValueAsString(ImmutableUserCreateRequest.builder().username("Thor Odinson").build()))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());

        mockMvc.perform(
                post("/v1/users")
                .content(objectMapper.writeValueAsString(ImmutableUserCreateRequest.builder().username("Wolverine").build()))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
        
        MessageCreateRequest messageCreateRequest = ImmutableMessageCreateRequest.builder().sender("Thor Odinson")
                .recipient("Wolverine").body("Deadpool is at it, again!").build();

        String messageId = JsonPath.read(
            mockMvc.perform(
                    post("/v1/messages")
                    .content(objectMapper.writeValueAsString(messageCreateRequest))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(), 
            "$.id"
        );
        
        mockMvc.perform(get("/v1/messages/" + messageId)).andExpect(status().isOk());
        
        MessageUpdateRequest messageUpdateRequest = ImmutableMessageUpdateRequest.builder().id(messageId).body("A change to the body of the content!").build();
        
        mockMvc.perform(
                put("/v1/messages")
                .content(objectMapper.writeValueAsString(messageUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk());
        
        mockMvc.perform(
                get("/v1/messages")
                .param("participants", "Wolverine")
        ).andExpect(status().isOk());

        LikeUpdateRequest likeUpdateRequest = ImmutableLikeUpdateRequest.builder().username("Thor Odinson").emoji(Emoji.THUMBS_UP).build();
        
        mockMvc.perform(
                put("/v1/messages/" + messageId + "/likes")
                .content(objectMapper.writeValueAsString(likeUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
        
        mockMvc.perform(delete("/v1/messages/" + messageId)).andExpect(status().isNoContent());

    }
    
    public static class ContainerSourceInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    "spring.data.mongodb.host=" + CONTAINER.getHost(),
                    "spring.data.mongodb.port=" + CONTAINER.getMappedPort(27017), "spring.data.mongodb.database=psvgs");
        }
    }

}
