package com.ilusha.marketplaceAPI.controllers;

import com.ilusha.marketplaceAPI.DTO.*;
import com.ilusha.marketplaceAPI.models.Listing;
import com.ilusha.marketplaceAPI.models.SavedListing;
import com.ilusha.marketplaceAPI.models.User;
import com.ilusha.marketplaceAPI.repository.ListingRepository;
import com.ilusha.marketplaceAPI.repository.SavedListingRepository;
import com.ilusha.marketplaceAPI.response.AllListingResponse;
import com.ilusha.marketplaceAPI.response.MyListingResponse;
import com.ilusha.marketplaceAPI.service.ListingService;
import com.ilusha.marketplaceAPI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/listing")
@RequiredArgsConstructor
public class ListingController {
    private final ModelMapper modelMapper;

    private final ListingService listingService;

    private final ListingRepository listingRepository;
    private final UserService userService;
    private final SavedListingRepository savedListingRepository;


    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createListing(@RequestBody CreateListingDTO listingDTO) {
        Listing listingToAdd = convertToListing(listingDTO);
        listingService.addListing(listingToAdd);


        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "return all listings that meet the criteria")
    @GetMapping("/search")
    public AllListingResponse searchListing(@RequestBody ListingSearchCriteriaDTO listingSearchCriteriaDTO) {
        return new AllListingResponse(listingRepository.findAll().stream()
                .filter(listing -> listingService.performFilter(listing, listingSearchCriteriaDTO))
                .map(this::convertToAllListingResponseDTO).collect(Collectors.toList()));
    }

    //TODO
    //make sure you dont see your own listings
    //make sure you cant add to favorites your ow listings.

    @PostMapping("/putToFavorite")
    public ResponseEntity<HttpStatus> putToFavorite(@RequestBody SingleListingDTO singleListingDTO) {
        SavedListing savedListing = convertToSavedListing(singleListingDTO);
        listingService.putToFavorite(savedListing);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @Operation(summary = "get user's FAVORITE listings")
    @GetMapping("/favorite")
    public AllListingResponse getFavorites(){
        System.out.println();

        User user = userService.getCurrentUser();

        return new AllListingResponse(savedListingRepository.findAllByOwner(user).stream()
                .map(this::convertToAllSavedListingResponseDTO).collect(Collectors.toList()));
    }



    @Operation(summary = "get user's listings")
    @GetMapping("/my")
    public MyListingResponse myListings(){
        User user = userService.getCurrentUser();

        return new MyListingResponse(listingRepository.findAllByOwner(user).stream()
                .map(this::convertToListingResponseDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "get all listings")
    @GetMapping("/")
    public AllListingResponse allListings(){

        return new AllListingResponse(listingRepository.findAll().stream()
                .map(this::convertToAllListingResponseDTO).collect(Collectors.toList()));
    }



    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> deleteListing(@RequestBody SingleListingDTO deleteListingDTO) {
        listingService.deleteListing(deleteListingDTO.getListing_id());
        return ResponseEntity.ok(HttpStatus.OK);
    }




    public Listing convertToListing(CreateListingDTO listingDTO) {
        return modelMapper.map(listingDTO, Listing.class);
    }

    public CreateListingDTO convertToListingDTO(Listing listing) {
        return modelMapper.map(listing, CreateListingDTO.class);
    }

    public ListingResponseDTO convertToListingResponseDTO(Listing listing) {
        ListingResponseDTO listingResponseDTO = modelMapper.map(listing, ListingResponseDTO.class);
        listingResponseDTO.setListing_id(listing.getListing_id());
        return listingResponseDTO;

    }

    private AllListingResponseDTO convertToAllListingResponseDTO(Listing listing) {
        AllListingResponseDTO allListingResponseDTO = modelMapper.map(listing, AllListingResponseDTO.class);

        allListingResponseDTO.setContactInfo(listing.getOwner().getEmail());
        return allListingResponseDTO;
    }

    private AllListingResponseDTO convertToAllSavedListingResponseDTO(SavedListing savedListing) {
        Listing listing = listingRepository.findById(savedListing.getListing().getListing_id()).orElseThrow();
        AllListingResponseDTO allListingResponseDTO = modelMapper.map(listing, AllListingResponseDTO.class);
        allListingResponseDTO.setContactInfo(listing.getOwner().getEmail());
        return allListingResponseDTO;



    }

    private SavedListing convertToSavedListing(SingleListingDTO singleListingDTO) {
        SavedListing savedListing = modelMapper.map(singleListingDTO, SavedListing.class);
        savedListing.setOwner(userService.getCurrentUser());

        return savedListing;
    }


}
