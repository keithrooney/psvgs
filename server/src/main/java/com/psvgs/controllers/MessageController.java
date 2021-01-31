package com.psvgs.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.psvgs.managers.MessageManager;
import com.psvgs.models.ImmutableLike;
import com.psvgs.models.ImmutableMessage;
import com.psvgs.models.Message;
import com.psvgs.models.MessageQuery;
import com.psvgs.requests.LikeUpdateRequest;
import com.psvgs.requests.MessageCreateRequest;
import com.psvgs.requests.MessageUpdateRequest;

@RestController
@RequestMapping("/v1/messages")
public class MessageController {

    private MessageManager messageManager;

    public MessageController(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Message create(@RequestBody MessageCreateRequest request) {
        return messageManager.create(ImmutableMessage.builder().sender(request.getSender())
                .recipient(request.getRecipient()).body(request.getBody()).build());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Message update(@RequestBody MessageUpdateRequest request) {
        return messageManager.update(ImmutableMessage.builder().id(request.getId()).body(request.getBody()).build());
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Message findById(@PathVariable String id) {
        Optional<Message> optional = messageManager.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable String id) {
        messageManager.deleteById(id);
    }
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Message> query(MessageQuery query) {
        return messageManager.query(query);
    }
    
    @PutMapping(path = "/{id}/likes", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void like(@PathVariable String id, @RequestBody LikeUpdateRequest like) {
        messageManager.like(id, ImmutableLike.builder().username(like.getUsername()).emoji(like.getEmoji()).build());
    }

}
