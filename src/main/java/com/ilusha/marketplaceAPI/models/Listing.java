package com.ilusha.marketplaceAPI.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name="listing")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer listing_id;

    @NotBlank(message = "The name of the listing is mandatory")
    @Size(min=3, max=30, message = "The name should be from 3 to 30 characters!")
    private String name;

    @NotBlank(message = "category is mandatory")
    private String category;

    @NotNull(message = "Price cannot be null")
    private Double price;

    private String photo_ref;

    @NotNull(message = "The location cannot be null!")
    @Size(min=1, max=100, message = "The location should be from 1 to 100 characters!")
    private String location;

    @NotNull(message = "The description cannot be null!")
    @Size(min=1, max=100, message = "The description should be from 1 to 100 characters!")
    private String description;

    private LocalDateTime listing_date;




    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

}
