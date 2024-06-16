package com.ilusha.negrJWT.controllers;

import com.ilusha.negrJWT.auth.AuthenticationRequest;
import com.ilusha.negrJWT.auth.AuthenticationResponse;
import com.ilusha.negrJWT.auth.AuthenticationService;
import com.ilusha.negrJWT.auth.RegisterRequest;
import com.ilusha.negrJWT.util.AuthError;
import com.ilusha.negrJWT.util.AuthExceptionResponse;
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
