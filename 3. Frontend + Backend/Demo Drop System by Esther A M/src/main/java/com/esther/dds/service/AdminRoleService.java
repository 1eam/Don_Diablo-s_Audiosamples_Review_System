package com.esther.dds.service;

import com.esther.dds.domain.AdminRole;
import com.esther.dds.repositories.AdminRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminRoleService {
    private final AdminRoleRepository adminRoleRepository;

    public AdminRoleService(AdminRoleRepository adminRoleRepository) {
        this.adminRoleRepository = adminRoleRepository;
    }

    public AdminRole findByName(String name) {
        return adminRoleRepository.findByName(name);
    }
}