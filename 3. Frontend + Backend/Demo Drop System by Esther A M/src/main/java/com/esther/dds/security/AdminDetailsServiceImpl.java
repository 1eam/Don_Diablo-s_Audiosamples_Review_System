package com.esther.dds.security;

import com.esther.dds.domain.Admin;
import com.esther.dds.domain.BoUser;
import com.esther.dds.repositories.AdminRepository;
import com.esther.dds.repositories.BoUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminDetailsServiceImpl implements UserDetailsService {

    private AdminRepository adminRepository;

    public AdminDetailsServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if( !admin.isPresent() ) {
            throw new UsernameNotFoundException(email);
        }
        return admin.get();
    }
}