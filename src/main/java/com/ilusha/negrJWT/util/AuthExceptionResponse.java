package com.ilusha.negrJWT.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthExceptionResponse {
    private String message;
    private long timestamp;

    public AuthExceptionResponse(String message, long timestamp) {
        this.timestamp = timestamp;
        this.message = message;
    }
}
