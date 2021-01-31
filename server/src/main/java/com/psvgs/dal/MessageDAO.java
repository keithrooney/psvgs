package com.psvgs.dal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.psvgs.models.ImmutableMessage;
import com.psvgs.models.Message;
import com.psvgs.models.MessageQuery;

@Repository
public class MessageDAO implements QueryableDAO<Message, MessageQuery> {

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
        return mongoTemplate.save(clone, DEFAULT_COLLECTION_NAME);
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

    @Override
    public List<Message> query(MessageQuery messageQuery) {
        Criteria criteria = new Criteria();
        criteria.orOperator(
            Criteria.where("sender").in(messageQuery.getParticipants()),
            Criteria.where("recipient").in(messageQuery.getParticipants()) 
        );
        int page =  messageQuery.getPage() < 0 ? 1 : messageQuery.getPage();
        int pageSize = messageQuery.getPageSize() < 0 ? 25 : messageQuery.getPageSize();
        int offset = Math.max(0, (page - 1) * pageSize);
        List<ImmutableMessage> result = mongoTemplate.find(Query.query(criteria).skip(offset).limit(pageSize),
                ImmutableMessage.class, DEFAULT_COLLECTION_NAME);
        return result.stream().map(it -> it).collect(Collectors.toList());
    }

}
