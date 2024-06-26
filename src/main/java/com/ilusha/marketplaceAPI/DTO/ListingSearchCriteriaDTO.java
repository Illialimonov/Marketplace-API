package com.ilusha.marketplaceAPI.DTO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class ListingSearchCriteriaDTO {
    private String name;
    private String category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
