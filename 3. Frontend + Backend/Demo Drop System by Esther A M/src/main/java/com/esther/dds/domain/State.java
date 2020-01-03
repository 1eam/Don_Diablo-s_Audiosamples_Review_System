package com.esther.dds.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class State extends Auditable{

    @Id
    @GeneratedValue
    private Long id;
    private String stateName;
    private String message;

}
