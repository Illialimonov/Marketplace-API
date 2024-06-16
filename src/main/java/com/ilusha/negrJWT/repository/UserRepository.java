package com.ilusha.negrJWT.repository;

import com.ilusha.negrJWT.models.Listing;
import com.ilusha.negrJWT.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);



}
