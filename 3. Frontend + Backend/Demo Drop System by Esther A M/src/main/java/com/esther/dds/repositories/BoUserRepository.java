package com.esther.dds.repositories;

import com.esther.dds.domain.BoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoUserRepository extends JpaRepository<BoUser, Long> {

    Optional<BoUser> findByEmail(String email);

    Optional<BoUser> findByEmailAndActivationCode(String email, String activationCode);

}