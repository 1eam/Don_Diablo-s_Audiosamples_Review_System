package com.esther.dds.service;

import com.esther.dds.domain.State;
import com.esther.dds.repositories.StateNameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StateService {
    private final StateNameRepository stateNameRepository;

    public StateService(StateNameRepository stateNameRepository) {
        this.stateNameRepository = stateNameRepository;
    }

    public State findByStateName(String string){
        return stateNameRepository.findByStateName(string);
    }

    public State save(State state){
        return stateNameRepository.save(state);
    }

}
