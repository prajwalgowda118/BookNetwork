package com.scaler.booknetwork.booknetwork.Models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private LocalDateTime expiryAt;
    private LocalDateTime createdAt;

    private LocalDateTime validatedAt;

    @ManyToOne()
    @JoinColumn(name="user_id", nullable=false)
    private User user;



}
