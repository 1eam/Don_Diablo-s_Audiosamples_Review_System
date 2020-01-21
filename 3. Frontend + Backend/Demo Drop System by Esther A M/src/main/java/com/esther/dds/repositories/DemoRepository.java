package com.esther.dds.repositories;

import com.esther.dds.domain.Demo;
import com.esther.dds.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DemoRepository extends JpaRepository <Demo, Long> {

    public List<Demo> findByStateStateName(String stateName);

    public List<Demo> findByUser(Optional<User> user);




//    Demo findWhereReviewstate=2 (But this should be done in a service layer)
}
