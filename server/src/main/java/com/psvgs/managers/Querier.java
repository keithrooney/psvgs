package com.psvgs.managers;

import java.util.List;

import com.psvgs.models.Query;

public interface Querier<T, Q extends Query> {
    List<T> query(Q query);
}
