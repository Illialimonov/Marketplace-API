package com.ilusha.marketplaceAPI.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateListingDTO {
    public String name;
    public String category;
    public double price;

    public String location;
    public String description;


}
