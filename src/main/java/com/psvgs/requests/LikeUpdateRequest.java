package com.psvgs.requests;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.psvgs.models.Emoji;

@Value.Immutable
@JsonSerialize(as = ImmutableLikeUpdateRequest.class)
@JsonDeserialize(as = ImmutableLikeUpdateRequest.class)
public interface LikeUpdateRequest {
    Emoji getEmoji();
    String getUsername();
}
