package com.ilusha.marketplaceAPI.service;


import com.ilusha.marketplaceAPI.models.RefreshToken;
import com.ilusha.marketplaceAPI.repository.RefreshTokenRepository;
import com.ilusha.marketplaceAPI.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(String email){
        RefreshToken refreshToken = RefreshToken.builder()
                .owner(userRepository.findByEmail(email).orElseThrow())
                .token(UUID.randomUUID().toString())
                .expiryDate(new Date(System.currentTimeMillis()+172800000)) // set expiry of refresh token to 10 minutes - you can configure it application.properties file
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(new Date(System.currentTimeMillis()).after(token.getExpiryDate())){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

}
