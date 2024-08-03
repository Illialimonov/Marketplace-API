package com.ilusha.marketplaceAPI.repository;


import com.ilusha.marketplaceAPI.models.SavedListing;
import com.ilusha.marketplaceAPI.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public interface SavedListingRepository extends JpaRepository<SavedListing, Integer> {
    List<SavedListing> findAllByOwner(User owner);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM saved_listings WHERE listing_id = :listing_id", nativeQuery = true)
    void deleteByListing_idFromSaved(@Param("listing_id") int listing_id);



}
