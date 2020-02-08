package com.esther.dds.security;

import com.esther.dds.domain.BoUser;
import com.esther.dds.repositories.BoUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BoUserDetailsServiceImpl implements UserDetailsService {

    private BoUserRepository boUserRepository;

    public BoUserDetailsServiceImpl(BoUserRepository boUserRepository) {
        this.boUserRepository = boUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<BoUser> boUser = boUserRepository.findByEmail(email);
        if( !boUser.isPresent() ) {
            throw new UsernameNotFoundException(email);
        }
        return boUser.get();
    }
}