package com.esther.dds.repositories;

import com.esther.dds.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateNameRepository extends JpaRepository <State, Long> {

   public State findByStateName(String string);

}
