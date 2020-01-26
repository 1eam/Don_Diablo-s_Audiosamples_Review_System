package com.esther.dds.service;

import com.esther.dds.domain.State;
import com.esther.dds.repositories.StateNameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StateService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final StateNameRepository stateNameRepository;

    public StateService(StateNameRepository stateNameRepository) {
        this.stateNameRepository = stateNameRepository;
    }


    public State findByStateName(String string){
        return stateNameRepository.findByStateName(string);
    }

    public Optional<State> findById(Long id){
        return stateNameRepository.findById(id);
    }

    public State save(State state){
        return stateNameRepository.save(state);
    }

    public State getOne(Long id){
        return stateNameRepository.getOne(id);
    }
}
