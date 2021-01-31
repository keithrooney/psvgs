package com.psvgs.requests;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableMessageUpdateRequest.class)
@JsonDeserialize(as = ImmutableMessageUpdateRequest.class)
public interface MessageUpdateRequest {
    String getId();
    String getBody();
}
