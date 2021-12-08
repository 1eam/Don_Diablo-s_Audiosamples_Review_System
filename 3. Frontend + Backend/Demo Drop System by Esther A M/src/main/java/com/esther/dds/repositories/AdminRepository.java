package com.esther.dds.repositories;

import com.esther.dds.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByEmailAndActivationCode(String email, String activationCode);
}