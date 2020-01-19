package com.esther.dds.service;

import com.esther.dds.domain.Demo;
import com.esther.dds.repositories.DemoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DemoService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final DemoRepository demoRepository;

    public DemoService(DemoRepository userRepository) {
        this.demoRepository = userRepository;
    }


    public List<Demo> findAll() {
        return demoRepository.findAll();
    }

    public Optional<Demo> findById(Long id){
        return demoRepository.findById(id);
    }

    public List<Demo> findByUser(Long id) {
        return demoRepository.findByUser(id);
    }

    public Demo save(Demo demo){
        return demoRepository.save(demo);
    }

    public void delete (Demo demo){
        demoRepository.delete(demo);
    }

    public List<Demo> findByStateStateName(String stateName) {
    return demoRepository.findByStateStateName (stateName);
    }
}