package com.esther.dds.config;

import com.esther.dds.domain.Admin;
import com.esther.dds.domain.BoUser;
import com.esther.dds.domain.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        //Hardcoded data (demo) (because of Auditable) will call for the user, but it wont exist. So the uploader will be set to admin
        if(SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return Optional.of("admin@gmail.com");
        }
        //first only if the current logged in user's class-name is "Users"
        //get the email of the current user ...
        //else its BoUser and get us that email
        //else its an AdminUser and get us that email
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().getName()=="com.esther.dds.domain.User"){
            return Optional.of(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        }
        else if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().getName()=="com.esther.dds.domain.BoUser"){
            return Optional.of(((BoUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        } else{
            return Optional.of(((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        }
    }
}


