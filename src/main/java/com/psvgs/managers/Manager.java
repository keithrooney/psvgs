package com.psvgs.managers;

import java.util.Optional;

public interface Manager<T> {
    Optional<T> findById(String id);
    T create(T object);
    T update(T object);
    void deleteById(String id);
}
