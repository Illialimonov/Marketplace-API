package com.ilusha.marketplaceAPI.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 5, max = 30, message = "First Name must be between 5 and 30 characters long")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "First Name must not contain numbers or special characters")
    @Column(name="first_name")
    @NotNull
    private String firstName;

    @Size(min = 5, max = 30, message = "Last Name must be between 5 and 30 characters long")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Last Name must not contain numbers or special characters")
    @Column(name="last_name")
    @NotNull
    private String lastName;

    @NotBlank(message = "The email of the listing is mandatory")
    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String role;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Listing> listings;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<SavedListing> savedListings;

    @OneToOne(mappedBy = "owner", fetch = FetchType.EAGER)
    private RefreshToken refreshToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
