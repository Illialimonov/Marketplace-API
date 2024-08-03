package com.ilusha.marketplaceAPI.service;

import com.ilusha.marketplaceAPI.repository.UserRepository;
import com.ilusha.marketplaceAPI.models.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);
    }


    public User getCurrentUser() {
        try{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (User) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow();}
        catch (RuntimeException e){
            throw new ClassCastException("Not user found!");
        }
    }

    public void adminCall() {

        System.out.println(getCurrentUser().getRole());
        System.out.println("You are the admin!");

    }
}
