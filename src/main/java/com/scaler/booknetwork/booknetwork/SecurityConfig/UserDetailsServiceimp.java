package com.scaler.booknetwork.booknetwork.SecurityConfig;

import com.scaler.booknetwork.booknetwork.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserDetailsServiceimp implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails userDetails= userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        return userDetails;
    }
}
