package com.esther.dds.Repositories;

import com.esther.dds.domain.StateName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateNameRepository extends JpaRepository <StateName, Long> {
}
