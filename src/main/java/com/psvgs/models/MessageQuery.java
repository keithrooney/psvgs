package com.psvgs.models;

import java.util.Set;

import org.immutables.value.Value;

@Value.Immutable
public interface MessageQuery extends Query {
    Set<String> getParticipants();
}
