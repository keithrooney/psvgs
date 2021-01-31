package com.psvgs.managers;

import java.util.Objects;
import java.util.Optional;

import com.psvgs.dal.DAO;
import com.psvgs.models.ImmutableUser;
import com.psvgs.models.User;

public class UserManager implements Manager<User> {

    private DAO<User> userDAO;

    @Override
    public Optional<User> findById(String id) {
        return userDAO.findById(Objects.requireNonNull(id));
    }

    @Override
    public User create(User user) {
        return userDAO.create(ImmutableUser.copyOf(user));
    }

    @Override
    public User update(User user) {
        return userDAO.update(Objects.requireNonNull(user));
    }

    @Override
    public void deleteById(String id) {
        userDAO.deleteById(Objects.requireNonNull(id));
    }

}
