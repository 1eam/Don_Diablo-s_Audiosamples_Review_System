package com.esther.dds.repositories;

import com.esther.dds.domain.BoRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoRoleRepository extends JpaRepository<BoRole,Long> {
    BoRole findByName(String name);
}
