package com.psvgs.models;

import java.time.LocalDateTime;

import org.immutables.value.Value;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableUser.class)
@JsonDeserialize(as = ImmutableUser.class)
public interface User extends Entity {

    String getUsername();

    @Nullable
    @Value.Auxiliary
    LocalDateTime getCreatedAt();

    @Nullable
    @Value.Auxiliary
    LocalDateTime getUpdatedAt();

}
