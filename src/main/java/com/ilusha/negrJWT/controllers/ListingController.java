package com.ilusha.negrJWT.controllers;

import com.ilusha.negrJWT.DTO.*;
import com.ilusha.negrJWT.models.Listing;
import com.ilusha.negrJWT.models.SavedListing;
import com.ilusha.negrJWT.models.User;
import com.ilusha.negrJWT.repository.ListingRepository;
import com.ilusha.negrJWT.repository.SavedListingRepository;
import com.ilusha.negrJWT.response.AllListingResponse;
import com.ilusha.negrJWT.response.MyListingResponse;
import com.ilusha.negrJWT.service.ListingService;
import com.ilusha.negrJWT.service.UserService;
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

    @GetMapping("/favorite")
    public AllListingResponse getFavorites(){
        System.out.println();

        User user = userService.getCurrentUser();

        return new AllListingResponse(savedListingRepository.findAllByOwner(user).stream()
                .map(this::convertToAllSavedListingResponseDTO).collect(Collectors.toList()));
    }




    @GetMapping("/my")
    public MyListingResponse myListings(){
        User user = userService.getCurrentUser();

        return new MyListingResponse(listingRepository.findAllByOwner(user).stream()
                .map(this::convertToListingResponseDTO).collect(Collectors.toList()));
    }

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
