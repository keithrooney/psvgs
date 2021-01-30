package com.psvgs.managers;

import java.util.Objects;
import java.util.Optional;

import com.psvgs.dal.DAO;
import com.psvgs.models.ImmutableMessage;
import com.psvgs.models.Message;

public class MessageManager implements Manager<Message> {

    private DAO<Message> messageDAO;

    public MessageManager(DAO<Message> messageDAO) {
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

}
