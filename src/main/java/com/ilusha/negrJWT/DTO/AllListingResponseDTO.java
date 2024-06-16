package com.ilusha.negrJWT.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllListingResponseDTO {
    private Integer listing_id;
    private String name;
    private Double price;
    private String location;
    private String description;
    private String contactInfo;
}
