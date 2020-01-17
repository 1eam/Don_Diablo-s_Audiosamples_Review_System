package com.esther.dds.repositories;

import com.esther.dds.domain.Demo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemoRepository extends JpaRepository <Demo, Long> {

    public List<Demo> findByStateStateName(String stateName);


//    Demo findWhereReviewstate=2 (But this should be done in a service layer)
}
