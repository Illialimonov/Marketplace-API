package com.ilusha.negrJWT.models;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name="saved_listings")
public class SavedListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User owner;

    @ManyToOne
    @JoinColumn(name="listing_id", referencedColumnName = "listing_id")
    public Listing listing;





}
