package com.psvgs.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.psvgs.managers.Manager;
import com.psvgs.models.ImmutableUser;
import com.psvgs.models.User;
import com.psvgs.requests.UserCreateRequest;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private Manager<User> userManager;

    public UserController(Manager<User> userManager) {
        this.userManager = userManager;
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody UserCreateRequest request) {
        return userManager.create(ImmutableUser.builder().username(request.getUsername()).build());
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User findById(@PathVariable String id) {
        Optional<User> optional = userManager.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    
}
