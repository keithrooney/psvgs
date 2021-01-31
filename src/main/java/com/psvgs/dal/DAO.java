package com.psvgs.dal;

import java.util.Optional;

public interface DAO<T> {
    Optional<T> findById(String id);
    T create(T object);
    T update(T object);
    void deleteById(String id);
}
