package com.ilusha.marketplaceAPI.response;

import com.ilusha.marketplaceAPI.DTO.AllListingResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class AllListingResponse {
    private List<AllListingResponseDTO> listings;

    public AllListingResponse(List<AllListingResponseDTO> listings) {
        this.listings = listings;
    }
}
