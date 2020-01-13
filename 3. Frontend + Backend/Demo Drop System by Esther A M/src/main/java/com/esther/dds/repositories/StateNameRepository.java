package com.esther.dds.repositories;

import com.esther.dds.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateNameRepository extends JpaRepository <State, Long> {

}
