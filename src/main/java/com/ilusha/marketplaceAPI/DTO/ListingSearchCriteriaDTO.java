package com.ilusha.marketplaceAPI.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;

@Getter
@Setter
public class ListingSearchCriteriaDTO {
    private String name;
    private ArrayList<String> categories;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
