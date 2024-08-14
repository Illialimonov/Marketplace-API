package com.ilusha.marketplaceAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilusha.marketplaceAPI.DTO.CreateListingDTO;
import com.ilusha.marketplaceAPI.DTO.NewPassDTO;
import com.ilusha.marketplaceAPI.auth.*;
import com.ilusha.marketplaceAPI.config.JwtService;
import com.ilusha.marketplaceAPI.logging.LogExecution;
import com.ilusha.marketplaceAPI.models.Listing;
import com.ilusha.marketplaceAPI.models.RefreshToken;
import com.ilusha.marketplaceAPI.models.User;
import com.ilusha.marketplaceAPI.service.RefreshTokenService;
import com.ilusha.marketplaceAPI.service.UserService;
import com.ilusha.marketplaceAPI.util.AuthError;
import com.ilusha.marketplaceAPI.util.AuthExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")

@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestPart(value = "account_pic", required = false) MultipartFile file, @RequestPart("register_data") String registerData) {
        if (file == null){
            service.register(registerData);
        } else {
            service.register(file, registerData);
            return ResponseEntity.status(HttpStatus.CREATED).body("Account successfully changed");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Account successfully changed");
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
//        service.register(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Account successfully created");
//    }


    @Operation(summary = "send the code to email to change the password. Then insert to /changepassword below.")
    @PostMapping("/reset")
    public ResponseEntity<HttpStatus> sendResetToken(@RequestBody ResetRequest request) {
        service.sendPassToken(request.getEmail());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/changepassword")
    public ResponseEntity<HttpStatus> changePassword(@RequestBody NewPassDTO request) {
        service.changePass(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/currentusername")
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }


    @PostMapping("/refreshtoken")
    public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshTokenRequestDTO.getToken());

        if (optionalRefreshToken.isEmpty()) {
            // If the refresh token is not found, throw an exception
            throw new RuntimeException("Refresh Token is not in DB..!!");
        }

        // Get the refresh token from the Optional
        RefreshToken refreshToken = optionalRefreshToken.get();

        // Verify the expiration of the refresh token
        try {
            refreshToken = refreshTokenService.verifyExpiration(refreshToken);
        } catch (RuntimeException e) {
            // If the refresh token is expired, an exception will be thrown
            throw new RuntimeException(e.getMessage());
        }

        // Get the user information associated with the refresh token
        User user = refreshToken.getOwner();
        // Generate a new access token using the user's username
        String accessToken = jwtService.generateToken(user);


        // Create and return the response containing the new access token and the refresh token
        HashMap<String,String> userDetails = new HashMap<>();
        userDetails.put("id", user.getId().toString());
        userDetails.put("email", user.getEmail());
        userDetails.put("firstName",user.getFirstName());
        userDetails.put("lastName",user.getLastName());
        userDetails.put("role",user.getRole());
        userDetails.put("profile_pic",user.getProfilePicture());

        return AuthenticationResponse.builder()
                .access_token(accessToken)
                .refresh_token(refreshToken.getToken())
                .userDetails(userDetails)
                .build();
    }


    @ExceptionHandler
    private ResponseEntity<AuthExceptionResponse> handleException(AuthError e) {
        AuthExceptionResponse response = new AuthExceptionResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
