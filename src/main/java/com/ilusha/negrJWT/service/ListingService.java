package com.ilusha.negrJWT.service;

import com.ilusha.negrJWT.DTO.ListingSearchCriteriaDTO;
import com.ilusha.negrJWT.DTO.SingleListingDTO;
import com.ilusha.negrJWT.models.Listing;
import com.ilusha.negrJWT.models.SavedListing;
import com.ilusha.negrJWT.models.User;
import com.ilusha.negrJWT.repository.ListingRepository;
import com.ilusha.negrJWT.repository.SavedListingRepository;
import com.ilusha.negrJWT.repository.UserRepository;
import com.ilusha.negrJWT.util.ListingOperationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class ListingService {
    public final ListingRepository listingRepository;
    public final UserRepository userRepository;
    public final UserService userService;
    public final ArrayList<String> categories; //TODO
    public final SavedListingRepository savedListingRepository;


    public void addListing(Listing listing) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (User) authentication.getPrincipal();
        System.out.println(userDetails);
        listing.setOwner(userRepository.findByEmail(userDetails.getUsername()).orElseThrow());




        listing.setListing_date(LocalDateTime.now());

        if(categoryExists(listing))
            listingRepository.save(listing);


    }

    private boolean categoryExists(Listing listing) {
        String cata = listing.getCategory();

        if(categories.contains(cata)){
            return true;
        }

        throw new ListingOperationException("Such category doesn't exist. Please type 1 of the followings:" +
                "\n1) Electronics" +
                "\n2) Fashion" +
                "\n3) Home and Garden" +
                "\n4) Automotive" +
                "\n5) Health and Beauty" +
                "\n6) Sports and Outdoors" +
                "\n7) Toys and Games");
    }

    public void deleteListing(int id){
        if(userOwnsListing(id)){
            listingRepository.deleteByListing_id(id);
        }
    }

    public void putToFavorite(SavedListing singleListing){
        savedListingRepository.save(singleListing);
    }

    public boolean userOwnsListing(int id){
        User user = userService.getCurrentUser();
        List<Listing> usersListings = listingRepository.findAllByOwner(user);
        for (Listing listing:usersListings){
            if (listing.getListing_id()==id){
                return true;
            }
        }

        throw new ListingOperationException("You are not the owner of the following listing!");
    }


    public ListingService(ListingRepository listingRepository, UserRepository userRepository, UserService userService, ArrayList<String> categories, SavedListingRepository savedListingRepository) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.categories = categories;
        this.savedListingRepository = savedListingRepository;



        categories.add("Electronics");
        categories.add("Fashion");
        categories.add("Home and Garden");
        categories.add("Automotive");
        categories.add("Health and Beauty");
        categories.add("Sports and Outdoors");
        categories.add("Toys and Games");
        categories.add("other");
    }


    public boolean performFilter(Listing listing, ListingSearchCriteriaDTO criteria) {
        boolean priceInRange = true;
        boolean nameMatches = true;
        boolean categoryMatches = true;

        if (criteria.getMinPrice() != null) {
            priceInRange = listing.getPrice() >= criteria.getMinPrice().doubleValue();
        }
        if (criteria.getMaxPrice() != null) {
            priceInRange = priceInRange && listing.getPrice() <= criteria.getMaxPrice().doubleValue();
        }
        if (criteria.getName() != null) {
            nameMatches = listing.getName().startsWith(criteria.getName());
        }
        if (criteria.getCategory() != null) {
            categoryMatches = listing.getCategory().startsWith(criteria.getCategory());
        }

         return priceInRange && nameMatches && categoryMatches;


    }

}
