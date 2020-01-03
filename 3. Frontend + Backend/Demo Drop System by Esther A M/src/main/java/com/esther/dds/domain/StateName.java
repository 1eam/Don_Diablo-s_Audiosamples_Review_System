package com.esther.dds.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class StateName extends Auditable{
    private Long id;
    private String name;
    private String message;
    private Date lastModifiedOn;

//    One statename can have many Demo's'
    @OneToMany(mappedBy = "stateName")
    private Demo demo;
}
