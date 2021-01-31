package com.psvgs.managers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.psvgs.dal.MessageDAO;
import com.psvgs.dal.UserDAO;
import com.psvgs.models.ImmutableMessage;
import com.psvgs.models.Message;
import com.psvgs.models.MessageQuery;

@Service
public class MessageManager implements QueryableManager<Message, MessageQuery> {

    private MessageDAO messageDAO;
    
    private UserDAO userDAO;

    public MessageManager(MessageDAO messageDAO, UserDAO userDAO) {
        this.messageDAO = messageDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Optional<Message> findById(String id) {
        return messageDAO.findById(id);
    }

    @Override
    public Message create(Message message) {
        userDAO.findByUsername(message.getSender()).orElseThrow();
        userDAO.findByUsername(message.getRecipient()).orElseThrow();
        return messageDAO.create(ImmutableMessage.copyOf(message));
    }

    @Override
    public Message update(Message message) {
        return messageDAO.update(ImmutableMessage.copyOf(message));
    }

    @Override
    public void deleteById(String id) {
        messageDAO.deleteById(Objects.requireNonNull(id));
    }

    @Override
    public List<Message> query(MessageQuery query) {
        return messageDAO.query(Objects.requireNonNull(query));
    }

}
