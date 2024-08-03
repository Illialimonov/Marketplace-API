package com.ilusha.marketplaceAPI.auth;

import com.ilusha.marketplaceAPI.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String access_token;
    private String refresh_token;
    private HashMap<String, String> userDetails;

    //TODO Refresh token return with iuser details
    //ToDO Created when Registered
}
