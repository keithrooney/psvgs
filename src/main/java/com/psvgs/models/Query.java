package com.psvgs.models;

import org.immutables.value.Value;

public interface Query {

    public static final int DEFAULT_PAGE = 1;

    public static final int DEFAULT_PAGE_SIZE = 25;
    
    @Value.Default
    default int getPage() {
        return DEFAULT_PAGE;
    }
    
    @Value.Default
    default int getPageSize() {
        return DEFAULT_PAGE_SIZE;
    }
    
}
