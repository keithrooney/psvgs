package com.psvgs.models;

import java.time.LocalDateTime;

import org.immutables.value.Value;
import org.springframework.lang.Nullable;

@Value.Immutable
public interface User extends Entity {

    String getUsername();

    @Nullable
    @Value.Auxiliary
    LocalDateTime getCreatedAt();

    @Nullable
    @Value.Auxiliary
    LocalDateTime getUpdatedAt();

}
