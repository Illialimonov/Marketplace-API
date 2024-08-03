package com.ilusha.marketplaceAPI.service;

import com.ilusha.marketplaceAPI.DTO.ListingSearchCriteriaDTO;
import com.ilusha.marketplaceAPI.configS3.StorageService;
import com.ilusha.marketplaceAPI.models.Listing;
import com.ilusha.marketplaceAPI.models.SavedListing;
import com.ilusha.marketplaceAPI.models.User;
import com.ilusha.marketplaceAPI.repository.ListingRepository;
import com.ilusha.marketplaceAPI.repository.SavedListingRepository;
import com.ilusha.marketplaceAPI.repository.UserRepository;
import com.ilusha.marketplaceAPI.util.ListingOperationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    public final StorageService storageService;


//    public void addListing(Listing listing, MultipartFile file) {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (User) authentication.getPrincipal();
//        System.out.println(userDetails);
//        listing.setOwner(userRepository.findByEmail(userDetails.getUsername()).orElseThrow());
//
//
//        listing.setListing_date(LocalDateTime.now());
//        listing.setPhoto_ref(converToURLEncoded(storageService.uploadFile(file)));
//
//        if (categoryExists(listing)) listingRepository.save(listing);
//
//
//    }
    public void addListing(Listing listing, MultipartFile file) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (User) authentication.getPrincipal();
        System.out.println(userDetails);
        listing.setOwner(userRepository.findByEmail(userDetails.getUsername()).orElseThrow());


        listing.setListing_date(LocalDateTime.now());
        listing.setPhoto_ref(converToURLEncoded(storageService.uploadFile(file)));

        if (categoryExists(listing)) listingRepository.save(listing);

    }


    private String converToURLEncoded(String s) {
        String url = URLEncoder.encode(s, StandardCharsets.UTF_8).replace("+", "%20");
        return "https://marketplace-api-storage.s3.us-east-2.amazonaws.com/" + url;
    }

    private boolean categoryExists(Listing listing) {
        String cata = listing.getCategory();

        if (categories.contains(cata)) {
            return true;
        }

        throw new ListingOperationException("Such category doesn't exist. Please type 1 of the followings:" + "\n1) Electronics" + "\n2) Fashion" + "\n3) Home and Garden" + "\n4) Automotive" + "\n5) Health and Beauty" + "\n6) Sports and Outdoors" + "\n7) Toys and Games");
    }

    public void deleteListing(int id) {
        if (userOwnsListing(id)) {
            storageService.deleteFile(listingRepository.findById(id).orElseThrow().getPhoto_ref());
            deleteListingFromFavorite(id);
            listingRepository.deleteByListing_id(id);
        }
    }

    public void deleteListingFromFavorite(int id) {
        storageService.deleteFile(listingRepository.findById(id).orElseThrow().getPhoto_ref());
        savedListingRepository.deleteByListing_idFromSaved(id);

    }

    public void putToFavorite(SavedListing singleListing) {
        savedListingRepository.save(singleListing);
    }

    public boolean userOwnsListing(int id) {
        User user = userService.getCurrentUser();
        List<Listing> usersListings = listingRepository.findAllByOwner(user);
        for (Listing listing : usersListings) {
            if (listing.getListing_id() == id) {
                return true;
            }
        }

        throw new ListingOperationException("You are not the owner of the following listing!");
    }


    public ListingService(ListingRepository listingRepository, UserRepository userRepository, UserService userService, ArrayList<String> categories, SavedListingRepository savedListingRepository, StorageService storageService) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.categories = categories;
        this.savedListingRepository = savedListingRepository;
        this.storageService = storageService;


        categories.add("Electronics");
        categories.add("Fashion");
        categories.add("Home and Garden");
        categories.add("Automotive");
        categories.add("Health and Beauty");
        categories.add("Sports and Outdoors");
        categories.add("Toys and Games");
        categories.add("Other");
    }


    public boolean performFilter(Listing listing, String listingName, List<String> categories, Double minPrice, Double maxPrice) {
        boolean priceInRange = true;
        boolean nameMatches = true;
        boolean categoryMatches = true;

        if (minPrice != null) {
            priceInRange = listing.getPrice() >= minPrice;
        }
        if (maxPrice != null) {
            priceInRange = priceInRange && listing.getPrice() <= maxPrice;
        }
        if (listingName != null) {
            nameMatches = listing.getName().startsWith(listingName);
        }
        if (categories != null) {
            categoryMatches = categories.contains(listing.getCategory());
        }

//        if (listing.getOwner().getId().equals(userService.getCurrentUser().getId())) return false;

        return priceInRange && nameMatches && categoryMatches;


    }

}
