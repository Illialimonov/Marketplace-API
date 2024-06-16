package com.ilusha.negrJWT.repository;

import com.ilusha.negrJWT.models.Listing;
import com.ilusha.negrJWT.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer> {
    List<Listing> findAllByOwner(User owner);

    Optional<Listing> findById(int id);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM listing WHERE listing_id = :listing_id", nativeQuery = true)
    void deleteByListing_id(@Param("listing_id") int listing_id);

}
