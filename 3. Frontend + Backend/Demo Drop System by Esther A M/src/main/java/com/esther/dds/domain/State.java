package com.esther.dds.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class State extends AuditableState{

    @Id
    @GeneratedValue
    private Long id;
    @NonNull
    private String stateName;
    @NonNull
    private String message;

}
