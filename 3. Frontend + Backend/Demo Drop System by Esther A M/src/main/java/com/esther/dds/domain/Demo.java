package com.esther.dds.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Demo extends Auditable{

    @Id
    @GeneratedValue
    @NonNull
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String audioFileLocation;

    private Date createdOn;
    private Long uploadedBy;
    private Long reviewedBy;
    private Long lastModifiedBy;


    @OneToOne(mappedBy = "demo")
    @Column(name = "reviewState")
    private StateName stateName = new StateName(); //column called: reviewState

    //User

}
