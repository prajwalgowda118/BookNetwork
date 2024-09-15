package com.scaler.booknetwork.booknetwork.Repository;

import com.scaler.booknetwork.booknetwork.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   // User findByEmail(String email);
   // User findByUsername(String username);

    Optional<User> findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    User save(User user);

}
