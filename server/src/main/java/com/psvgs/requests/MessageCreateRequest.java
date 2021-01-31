package com.psvgs.requests;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableMessageCreateRequest.class)
@JsonDeserialize(as = ImmutableMessageCreateRequest.class)
public interface MessageCreateRequest {
    String getSender();
    String getRecipient(); 
    String getBody();
}
