package com.esther.dds.service;

import com.esther.dds.domain.BoRole;
import com.esther.dds.repositories.BoRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class BoRoleService {
    private final BoRoleRepository boRoleRepository;

    public BoRoleService(BoRoleRepository boRoleRepository) {
        this.boRoleRepository = boRoleRepository;
    }

    public BoRole findByName(String name) {
        return boRoleRepository.findByName(name);
    }
}