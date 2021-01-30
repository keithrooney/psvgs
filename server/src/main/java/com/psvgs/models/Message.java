package com.psvgs.models;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.immutables.value.Value;
import org.springframework.lang.Nullable;

@Value.Immutable
public interface Message {

    @Nullable
    String getId();

    Entity getRecipient();

    String getBody();
    
    @Value.Default
    default Map<Like, List<Entity>> getLikes() {
        return Collections.emptyMap();
    }

    @Nullable
    LocalDateTime getCreatedAt();

    @Nullable
    LocalDateTime getUpdatedAt();

}
