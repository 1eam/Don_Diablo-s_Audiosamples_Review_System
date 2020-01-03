package com.esther.dds.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Demo extends Auditable{

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String audioFileLocation;

//    private Date createdOn;
//    private Long uploadedBy;
//    private Long reviewedBy;
//    private Long lastEditedBy;


    //State LookupTable
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="State")
    private State state;

}
