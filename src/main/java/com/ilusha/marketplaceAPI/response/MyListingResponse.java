package com.ilusha.marketplaceAPI.response;

import com.ilusha.marketplaceAPI.DTO.ListingResponseDTO;

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
