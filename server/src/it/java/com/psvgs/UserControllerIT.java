package com.psvgs;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

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
import com.psvgs.requests.ImmutableUserCreateRequest;
import com.psvgs.requests.UserCreateRequest;

@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = UserControllerIT.ContainerSourceInitializer.class)
public class UserControllerIT {

    public static final MongoDBContainer CONTAINER = new MongoDBContainer("mongo:4.4.3");

    static {
        CONTAINER.start();
    }
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateIsOk() throws UnsupportedEncodingException, JsonProcessingException, Exception {

        UserCreateRequest request = ImmutableUserCreateRequest.builder().username("Deadpool").build();

        String userId = JsonPath.read(
            mockMvc.perform(
                    post("/v1/users")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(), 
            "$.id"
        );

        mockMvc.perform(get("/v1/users/" + userId)).andExpect(status().isOk());
        
    }
    
    @Test
    public void testFindByIdReturns404() throws UnsupportedEncodingException, JsonProcessingException, Exception {
        mockMvc.perform(get("/v1/users/" + UUID.randomUUID().toString())).andExpect(status().isNotFound());
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
