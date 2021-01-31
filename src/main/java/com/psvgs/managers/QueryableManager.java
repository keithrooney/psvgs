package com.psvgs.managers;

import java.util.List;

import com.psvgs.models.Query;

public interface QueryableManager<T, Q extends Query> extends Manager<T> {
    List<T> query(Q query);
}
