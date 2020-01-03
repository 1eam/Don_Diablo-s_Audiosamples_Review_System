package com.esther.dds.domain;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class User {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private String artistName;
    private String bio;
    private String profileImage;
    private Date createdOn;
    private Date lastModifiedOn;
}

// Must be aware of Demo
// User has many Demo's: oneToMany