package com.esther.dds.Repositories;

import com.esther.dds.domain.Demo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoRepository extends JpaRepository <Demo, Long> {

//    Demo findWhereReviewstate=2 (But this should be done in a service layer)
}
