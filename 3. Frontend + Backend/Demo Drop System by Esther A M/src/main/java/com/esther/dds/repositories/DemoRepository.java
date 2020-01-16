package com.esther.dds.repositories;

import com.esther.dds.domain.Demo;
import com.esther.dds.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public interface DemoRepository extends JpaRepository <Demo, Long> {

    public List<Demo> findByStateStateName(String stateName);

//    Demo findWhereReviewstate=2 (But this should be done in a service layer)
}
