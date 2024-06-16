package com.ilusha.negrJWT.repository;


import com.ilusha.negrJWT.models.Listing;
import com.ilusha.negrJWT.models.SavedListing;
import com.ilusha.negrJWT.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface SavedListingRepository extends JpaRepository<SavedListing, Integer> {
    List<SavedListing> findAllByOwner(User owner);



}
