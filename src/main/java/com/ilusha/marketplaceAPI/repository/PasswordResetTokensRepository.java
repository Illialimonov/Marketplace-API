package com.ilusha.marketplaceAPI.repository;

import com.ilusha.marketplaceAPI.models.PasswordResetTokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetTokensRepository extends JpaRepository<PasswordResetTokens, Integer> {

    @Transactional
    @Query(value = "SELECT * FROM password_reset_tokens WHERE user_id = :userId ORDER BY expired_at DESC LIMIT 1", nativeQuery = true)
    Optional<PasswordResetTokens> getLatestTokenForOwner(@Param("userId") int userId);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM password_reset_tokens WHERE user_id = :user_id", nativeQuery = true)
    void deleteAllByUser_id(@Param("user_id") int user_id);
}
