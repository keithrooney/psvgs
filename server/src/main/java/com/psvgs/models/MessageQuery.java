package com.psvgs.models;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface MessageQuery {
    Optional<String> getSender();
    Optional<String> getRecipient();
}
