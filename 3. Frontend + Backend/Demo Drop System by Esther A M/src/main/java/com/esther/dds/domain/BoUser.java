package com.esther.dds.domain;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class BoUser {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String lastName;
    private Date createdOn;
    private Date lastModifiedOn;
}
