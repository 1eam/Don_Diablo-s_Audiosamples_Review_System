package com.esther.dds.Repositories;

import com.esther.dds.domain.Demo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoRepository extends JpaRepository <Demo, Long> {

//    Demo findByReviewState
}
