package com.ilusha.negrJWT.response;

import com.ilusha.negrJWT.DTO.AllListingResponseDTO;
import com.ilusha.negrJWT.DTO.ListingResponseDTO;
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
