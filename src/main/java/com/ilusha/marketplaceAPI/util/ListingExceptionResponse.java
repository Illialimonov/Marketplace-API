package com.ilusha.marketplaceAPI.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListingExceptionResponse {
    private String message;
    private long timestamp;

    public ListingExceptionResponse(String message, long timestamp) {
        this.timestamp = timestamp;
        this.message = message;
    }
}

