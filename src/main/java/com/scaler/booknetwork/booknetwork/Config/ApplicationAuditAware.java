package com.scaler.booknetwork.booknetwork.Config;


import com.scaler.booknetwork.booknetwork.Models.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ApplicationAuditAware implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null ||
                !(authentication instanceof AnonymousAuthenticationToken)
                || !authentication.isAuthenticated())
        {
            return Optional.empty();
        }

        User user = (User) authentication.getPrincipal();
        return Optional.ofNullable(Math.toIntExact(user.getId()));
    }
}
