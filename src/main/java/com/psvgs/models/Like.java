package com.psvgs.models;

import org.immutables.value.Value;

@Value.Immutable
public interface Like {
    String getUsername();
    Emoji getEmoji();
}
