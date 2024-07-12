package com.ilusha.marketplaceAPI.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateListingDTO {
    @NotBlank(message = "The name of the listing is mandatory")
    @Size(min=3, max=30, message = "The name should be from 3 to 30 characters!")
    private String name;

    @NotBlank(message = "category is mandatory")
    private String category;

    @NotNull(message = "Price cannot be null")
    private Double price;


    @NotNull(message = "The location cannot be null!")
    @Size(min=1, max=100, message = "The location should be from 1 to 100 characters!")
    private String location;

    @NotNull(message = "The description cannot be null!")
    @Size(min=1, max=100, message = "The description should be from 1 to 100 characters!")
    private String description;


}
