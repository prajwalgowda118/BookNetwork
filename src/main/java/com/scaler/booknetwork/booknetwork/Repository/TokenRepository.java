package com.scaler.booknetwork.booknetwork.Repository;

import com.scaler.booknetwork.booknetwork.Models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);



}
