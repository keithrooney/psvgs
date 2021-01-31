package com.psvgs.models;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.immutables.value.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.lang.Nullable;

@Value.Style(
    of = "new", 
    allParameters = true,
    passAnnotations = {PersistenceConstructor.class}
)
@Value.Immutable
public abstract class Message {

    @PersistenceConstructor
    public Message() {
    }
    
    @Id
    @Nullable
    public abstract String getId();

    @Nullable
    public abstract String getSender();

    @Nullable
    public abstract String getRecipient(); 

    public abstract String getBody();

    @Value.Default
    public Map<Like, List<Entity>> getLikes() {
        return Collections.emptyMap();
    }

    @Nullable
    @Value.Auxiliary
    public abstract LocalDateTime getCreatedAt();

    @Nullable
    @Value.Auxiliary
    public abstract LocalDateTime getUpdatedAt();

}
