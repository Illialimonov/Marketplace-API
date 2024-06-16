package com.ilusha.negrJWT.models;

import jakarta.persistence.*;
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

    private String name;

    private String category;

    private Double price;

    private String location;

    private String description;

    private LocalDateTime listing_date;



    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User owner;

}
