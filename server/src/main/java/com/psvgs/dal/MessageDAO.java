package com.psvgs.dal;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.psvgs.models.ImmutableMessage;
import com.psvgs.models.Message;

@Repository
public class MessageDAO implements DAO<Message> {

    private static final String DEFAULT_COLLECTION_NAME = "messages";

    private MongoTemplate mongoTemplate;

    public MessageDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Message> findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        return Optional.ofNullable(mongoTemplate.findOne(query, ImmutableMessage.class, DEFAULT_COLLECTION_NAME));
    }

    @Override
    public Message create(Message message) {
        LocalDateTime timestamp = LocalDateTime.now();
        Message clone = ImmutableMessage.builder().from(message).createdAt(timestamp).updatedAt(timestamp).build();
        return mongoTemplate.insert(clone, DEFAULT_COLLECTION_NAME);
    }

    @Override
    public Message update(Message message) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(message.getId()));
        Update definition = new Update();
        definition.set("body", message.getBody());
        definition.set("updatedAt", LocalDateTime.now());
        mongoTemplate.updateFirst(query, definition, DEFAULT_COLLECTION_NAME);
        return message;
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(Objects.requireNonNull(id)));
        mongoTemplate.remove(query, ImmutableMessage.class, DEFAULT_COLLECTION_NAME);
    }

}
