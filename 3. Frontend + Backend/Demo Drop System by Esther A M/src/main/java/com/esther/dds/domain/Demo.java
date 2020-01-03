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
    private String title;
    @NonNull
    private String description;
    @NonNull
    private String audioFileLocation;
    @NonNull
    private Date createdOn;

    private Long uploadedBy;
    private Long reviewedBy;
    private Long reviewState;
    private Long lastModifiedBy;

//    StateNames
//    User

//    @ManyToOne
//    @JoinTable(name = "user_id")
//    private User user
}
