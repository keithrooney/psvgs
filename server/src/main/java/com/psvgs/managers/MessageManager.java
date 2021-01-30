package com.psvgs.managers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.psvgs.dal.MessageDAO;
import com.psvgs.models.ImmutableMessage;
import com.psvgs.models.Message;
import com.psvgs.models.MessageQuery;

@Service
public class MessageManager implements Manager<Message>, Querier<Message, MessageQuery> {

    private MessageDAO messageDAO;

    public MessageManager(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    @Override
    public Optional<Message> findById(String id) {
        return messageDAO.findById(id);
    }

    @Override
    public Message create(Message message) {
        return messageDAO.create(ImmutableMessage.copyOf(message));
    }

    public Message update(Message message) {
        return messageDAO.update(ImmutableMessage.copyOf(message));
    }

    public void deleteById(String id) {
        messageDAO.deleteById(Objects.requireNonNull(id));
    }

    @Override
    public List<Message> query(MessageQuery query) {
        return messageDAO.query(Objects.requireNonNull(query));
    }

}
