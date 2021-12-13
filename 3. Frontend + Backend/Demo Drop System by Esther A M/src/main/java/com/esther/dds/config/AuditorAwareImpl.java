package com.esther.dds.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import com.esther.dds.domain.Admin;
import com.esther.dds.domain.BoUser;
import com.esther.dds.domain.User;

/**
 * This class makes sure the components of the application are aware of the application's current auditor.
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        //If the hardcoded data (demo) calls for the user and it doesnt exist, the uploader will be set to admin
        if(SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            return Optional.of("admin@gmail.com");
        }
        //first only if the current logged in user's class-name is "Users"
        //get the email of the current user ...
        //else its BoUser and get us that email
        //else its an AdminUser and get us that email
        
        // TODO if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserInterface)
        //     return Optional.of(((UserInterface) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        
        
        
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


