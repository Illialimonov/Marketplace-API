package com.ilusha.marketplaceAPI.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name="password_reset_tokens")
public class PasswordResetTokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer token_id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User owner;

    private String token;

    private Date expired_at;

}
