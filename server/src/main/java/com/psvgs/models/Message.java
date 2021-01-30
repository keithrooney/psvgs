package com.psvgs.models;

import java.time.LocalDateTime;

import org.immutables.value.Value;
import org.springframework.lang.Nullable;

@Value.Immutable
public interface Message {

    @Nullable
    String getId();

    String getSender();

    String getRecipient();

    String getBody();

    @Nullable
    LocalDateTime getCreatedAt();

    @Nullable
    LocalDateTime getUpdatedAt();

}
