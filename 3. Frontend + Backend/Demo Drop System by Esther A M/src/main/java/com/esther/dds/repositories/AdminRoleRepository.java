package com.esther.dds.repositories;

import com.esther.dds.domain.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRoleRepository extends JpaRepository<AdminRole,Long> {
    AdminRole findByName(String name);
}
