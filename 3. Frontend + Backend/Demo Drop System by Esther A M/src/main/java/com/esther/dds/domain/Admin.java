package com.esther.dds.domain;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Admin {
    private Long id;
    private String username;
    private String password;
    private Date createdOn;
    private Date lastModifiedOn;
}
