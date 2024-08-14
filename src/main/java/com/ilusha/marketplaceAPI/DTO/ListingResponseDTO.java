package com.ilusha.marketplaceAPI.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListingResponseDTO {
    private Integer listing_id;
    private String name;
    private Double price;
    private String location;
    private String description;
    private String category;
    private String photo_ref;

    // Getters and setters
}