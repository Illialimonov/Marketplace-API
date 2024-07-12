package com.ilusha.marketplaceAPI.auth;

import com.ilusha.marketplaceAPI.DTO.NewPassDTO;
import com.ilusha.marketplaceAPI.config.JwtService;
import com.ilusha.marketplaceAPI.configS3.SendEmailService;
import com.ilusha.marketplaceAPI.logging.LogExecution;
import com.ilusha.marketplaceAPI.models.PasswordResetTokens;
import com.ilusha.marketplaceAPI.models.RefreshToken;
import com.ilusha.marketplaceAPI.repository.PasswordResetTokensRepository;
import com.ilusha.marketplaceAPI.repository.UserRepository;
import com.ilusha.marketplaceAPI.models.User;
import com.ilusha.marketplaceAPI.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 6;
    private static final Random RANDOM = new SecureRandom();
    private final PasswordResetTokensRepository passwordResetTokensRepository;
    private final SendEmailService sendEmailService;
    private final RefreshTokenService refreshTokenService;


    @LogExecution
    public AuthenticationResponse register(RegisterRequest input) {
    User user = new User();
            user.setFirstName(input.getFirstName());
            user.setLastName(input.getLastName());
            user.setEmail(input.getEmail());
            user.setPassword(passwordEncoder.encode(input.getPassword()));
            user.setRole("USER");

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().access_token(jwtToken).build();
    }

    public void sendPassToken(String email){
        if (userRepository.findByEmail(email).isEmpty()){
            throw new RuntimeException("User was not found");
        }

        PasswordResetTokens token = new PasswordResetTokens();
        token.setOwner(userRepository.findByEmail(email).orElseThrow());
        token.setExpired_at(new Date(System.currentTimeMillis()+300000));
        String resetCode = generateCode();
        token.setToken(resetCode);
        passwordResetTokensRepository.save(token);
        sendEmailService.sendEmail(email, resetCode);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        if(authentication.isAuthenticated()) {
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

            var jwtToken = jwtService.generateToken(user);

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());


            return AuthenticationResponse.builder().access_token(jwtToken).refresh_token(refreshToken.getToken()).build();
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }

    }


    public static String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    public PasswordResetTokens getLatestToken(NewPassDTO request){
        return passwordResetTokensRepository.getLatestTokenForOwner(userRepository.findByEmail(request.getEmail()).orElseThrow().getId()).orElseThrow();
    }

    public void changePass(NewPassDTO request) {
        // Retrieve the latest token once
        PasswordResetTokens resetToken = getLatestToken(request);

        // Check if the tokens are the same
        if (request.getCode().equals(resetToken.getToken())) {
            // Check if the token is not expired
            if (new Date(System.currentTimeMillis()).before(resetToken.getExpired_at())) {
                // Check if the passwords match
                if (request.getPassword().equals(request.getRepeatedPassword())) {
                    // Find the user by email
                    User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                            new RuntimeException("User not found"));

                    // Set the new password
                    user.setPassword(passwordEncoder.encode(request.getPassword()));

                    // Save the user
                    userRepository.save(user);

                    passwordResetTokensRepository.deleteAllByUser_id(user.getId());

                    // Optionally invalidate the token
                    // resetTokenRepository.delete(resetToken);

                    // Provide success feedback (e.g., log, return value, etc.)
                    System.out.println("Password reset successful");
                } else {
                    throw new RuntimeException("Passwords do not match");
                }
            } else {
                throw new RuntimeException("Token has expired");
            }
        } else {
            throw new RuntimeException("Invalid token");
        }
    }

}
