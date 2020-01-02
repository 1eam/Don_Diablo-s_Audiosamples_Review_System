package com.esther.dds.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class StateNames {
    private Long id;
    private String name;
    private String message;
    private Date lastModifiedOn;


}
