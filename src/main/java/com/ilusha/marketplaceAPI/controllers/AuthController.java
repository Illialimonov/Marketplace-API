package com.ilusha.marketplaceAPI.controllers;

import com.ilusha.marketplaceAPI.auth.AuthenticationRequest;
import com.ilusha.marketplaceAPI.auth.AuthenticationResponse;
import com.ilusha.marketplaceAPI.auth.AuthenticationService;
import com.ilusha.marketplaceAPI.auth.RegisterRequest;
import com.ilusha.marketplaceAPI.util.AuthError;
import com.ilusha.marketplaceAPI.util.AuthExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));

    }

    @GetMapping("/currentusername")
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(service.authenticate(request));

    }

    @ExceptionHandler
    private ResponseEntity<AuthExceptionResponse> handleException(AuthError e) {
        AuthExceptionResponse response = new AuthExceptionResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
