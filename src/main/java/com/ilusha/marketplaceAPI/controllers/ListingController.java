package com.ilusha.marketplaceAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilusha.marketplaceAPI.DTO.*;
import com.ilusha.marketplaceAPI.logging.LogExecution;
import com.ilusha.marketplaceAPI.models.Listing;
import com.ilusha.marketplaceAPI.models.SavedListing;
import com.ilusha.marketplaceAPI.models.User;
import com.ilusha.marketplaceAPI.repository.ListingRepository;
import com.ilusha.marketplaceAPI.repository.SavedListingRepository;
import com.ilusha.marketplaceAPI.response.AllListingResponse;
import com.ilusha.marketplaceAPI.response.MyListingResponse;
import com.ilusha.marketplaceAPI.service.ListingService;
import com.ilusha.marketplaceAPI.service.UserService;
import com.ilusha.marketplaceAPI.util.ListingExceptionResponse;
import com.ilusha.marketplaceAPI.util.ListingOperationException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/listing")
@RequiredArgsConstructor
public class ListingController {
    private final ModelMapper modelMapper;
    private final ListingService listingService;
    private final ListingRepository listingRepository;
    private final UserService userService;
    private final SavedListingRepository savedListingRepository;

    //TODO delete from favorite when deleteing object
    //TODO delete frm favorite in general


    @LogExecution
    @PostMapping(value = "/create")
    public ResponseEntity<?> createListing(@RequestPart("file") MultipartFile file, @RequestPart("listing_data") String listingData) {
        CreateListingDTO listingDTO;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            listingDTO = objectMapper.readValue(listingData, CreateListingDTO.class);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid listing data");
        }

        // Convert DTO to entity and add listing
        Listing listingToAdd = convertToListing(listingDTO);
        listingService.addListing(listingToAdd, file);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
//    @LogExecution
//    @PostMapping(value = "/create")
//    public ResponseEntity<?> createListing(@RequestPart("files") List<MultipartFile> files, @RequestPart("listing_data") String listingData) {
//        CreateListingDTO listingDTO;
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            listingDTO = objectMapper.readValue(listingData, CreateListingDTO.class);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid listing data");
//        }
//
//        // Convert DTO to entity and add listing
//        Listing listingToAdd = convertToListing(listingDTO);
//        listingService.addListing(listingToAdd, files);
//
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }




    @Operation(summary = "return all listings that meet the criteria")
    @GetMapping("/search")
    public AllListingResponse searchListing(@RequestParam(required = false) String listingName,
                                            @RequestParam(required = false) List<String> categories,
                                            @RequestParam(required = false) Double minPrice,
                                            @RequestParam(required = false) Double maxPrice)
            {
        return new AllListingResponse(listingRepository.findAll().stream().filter(listing -> listingService.performFilter(listing, listingName, categories, minPrice, maxPrice)).map(this::convertToAllListingResponseDTO).collect(Collectors.toList()));
    }


    @PostMapping("/putToFavorite")
    public ResponseEntity<HttpStatus> putToFavorite(@RequestBody SingleListingDTO singleListingDTO) {
        SavedListing savedListing = convertToSavedListing(singleListingDTO);
        listingService.putToFavorite(savedListing);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    @Operation(summary = "get user's FAVORITE listings")
    @GetMapping("/favorite")
    public AllListingResponse getFavorites() {
        System.out.println();

        User user = userService.getCurrentUser();

        return new AllListingResponse(savedListingRepository.findAllByOwner(user).stream().map(this::convertToAllSavedListingResponseDTO).collect(Collectors.toList()));
    }


    @Operation(summary = "get user's listings")
    @GetMapping("/my")
    public MyListingResponse myListings() {
        User user = userService.getCurrentUser();

        return new MyListingResponse(listingRepository.findAllByOwner(user).stream().map(this::convertToListingResponseDTO).collect(Collectors.toList()));
    }

    @Operation(summary = "get all listings")
    @GetMapping("/")
    public AllListingResponse allListings() {

        return new AllListingResponse(listingRepository.findAll().stream().map(this::convertToAllListingResponseDTO).collect(Collectors.toList()));
    }


    @DeleteMapping("/delete")
    public ResponseEntity<HttpStatus> deleteListing(@RequestBody SingleListingDTO deleteListingDTO) {
        listingService.deleteListing(deleteListingDTO.getListing_id());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/deleteFromFavorite")
    public ResponseEntity<HttpStatus> deleteListingFromFavorite(@RequestBody SingleListingDTO deleteListingDTO) {
        listingService.deleteListingFromFavorite(deleteListingDTO.getListing_id());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ListingExceptionResponse> handleException(ListingOperationException e) {
        ListingExceptionResponse response = new ListingExceptionResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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


        HashMap<String,String> sellerDetails = new HashMap<>();
        sellerDetails.put("sellerFirstName", listing.getOwner().getFirstName());
        sellerDetails.put("sellerLastName", listing.getOwner().getLastName());
        sellerDetails.put("sellerEmail", listing.getOwner().getEmail());

        allListingResponseDTO.setSellerDetails(sellerDetails);

        return allListingResponseDTO;
    }

    private AllListingResponseDTO convertToAllSavedListingResponseDTO(SavedListing savedListing) {
        Listing listing = listingRepository.findById(savedListing.getListing().getListing_id()).orElseThrow();
        AllListingResponseDTO allListingResponseDTO = modelMapper.map(listing, AllListingResponseDTO.class);
        HashMap<String,String> sellerDetails = new HashMap<>();
        sellerDetails.put("sellerFirstName", listing.getOwner().getFirstName());
        sellerDetails.put("sellerLastName", listing.getOwner().getLastName());
        sellerDetails.put("sellerEmail", listing.getOwner().getEmail());

        allListingResponseDTO.setSellerDetails(sellerDetails);
        return allListingResponseDTO;


    }

    private SavedListing convertToSavedListing(SingleListingDTO singleListingDTO) {
        SavedListing savedListing = modelMapper.map(singleListingDTO, SavedListing.class);
        savedListing.setOwner(userService.getCurrentUser());

        return savedListing;
    }


}
