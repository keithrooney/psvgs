package com.psvgs.models;

import org.immutables.value.Value;

@Value.Immutable
public interface User {
    String getId();
    String getUsername();
}
