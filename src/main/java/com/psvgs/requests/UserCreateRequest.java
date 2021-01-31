package com.psvgs.requests;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableUserCreateRequest.class)
@JsonDeserialize(as = ImmutableUserCreateRequest.class)
public interface UserCreateRequest {
    String getUsername();
}
