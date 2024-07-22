package com.ilusha.marketplaceAPI.repository;

import com.ilusha.marketplaceAPI.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query(value="delete from refresh_token where user_id = :user_id", nativeQuery = true)
    void deleteByUser_id(@Param("user_id") int user_id);

//    @Modifying
//    @Transactional
//    @Query(value="DELETE FROM listing WHERE listing_id = :listing_id", nativeQuery = true)
//    void deleteByListing_id(@Param("listing_id") int listing_id);
}
