package com.psvgs.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Message {
    String getId();
    String getSender();
    String getRecipient();
    String getBody();
}
