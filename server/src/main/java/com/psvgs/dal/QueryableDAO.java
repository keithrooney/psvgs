package com.psvgs.dal;

import java.util.List;

import com.psvgs.models.Query;

public interface QueryableDAO<T, Q extends Query> extends DAO<T> {
    List<T> query(Q query);
}
