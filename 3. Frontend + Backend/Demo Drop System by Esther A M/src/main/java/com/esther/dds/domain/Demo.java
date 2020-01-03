package com.esther.dds.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@Getter @Setter
@ToString
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



    //State LookupTable
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="State")
    private State state;

}
