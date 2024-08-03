package com.ilusha.marketplaceAPI.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class AllListingResponseDTO {
    private Integer listing_id;
    private String name;
    private Double price;
    private String category;
    private String location;
    private String description;
    private String photo_ref;
    private HashMap<String,String> sellerDetails;

}
