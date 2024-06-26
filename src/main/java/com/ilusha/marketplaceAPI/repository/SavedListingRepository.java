package com.ilusha.marketplaceAPI.repository;


import com.ilusha.marketplaceAPI.models.SavedListing;
import com.ilusha.marketplaceAPI.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SavedListingRepository extends JpaRepository<SavedListing, Integer> {
    List<SavedListing> findAllByOwner(User owner);



}
