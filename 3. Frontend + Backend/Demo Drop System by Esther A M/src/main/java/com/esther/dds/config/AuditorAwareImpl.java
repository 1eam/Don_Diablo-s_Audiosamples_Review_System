package com.esther.dds.config;

import com.esther.dds.domain.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        //Hardcoded data (demo) (because of Auditable) will call for the user, but it wont exist. So the uploader will be set to admin
        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            return Optional.of("admin@gmail.com");
        }
        //to find the user thats logged in
        return Optional.of(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
    }
}
