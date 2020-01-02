package com.esther.dds.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Demo {

    @Id
    @GeneratedValue
    @NonNull
    private Long id;
    @NonNull
    private String audioFileLocation;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private Date createdOn;

    private Long lastModifiedBy;
    private Long uploadedBy;
    private Long reviewedBy;
    private Long reviewState;

//    StateNames
//    User

//    @ManyToOne
//    @JoinTable(name = "user_id")
//    private User user
}
