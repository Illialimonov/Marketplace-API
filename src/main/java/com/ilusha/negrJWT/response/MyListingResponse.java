package com.ilusha.negrJWT.response;

import com.ilusha.negrJWT.DTO.ListingResponseDTO;

import java.util.List;

public class MyListingResponse {
    private List<ListingResponseDTO> listings;

    public MyListingResponse(List<ListingResponseDTO> listings) {
        this.listings = listings;
    }

    public List<ListingResponseDTO> getListings() {
        return listings;
    }

    public void setListings(List<ListingResponseDTO> listings) {
        this.listings = listings;
    }
}
