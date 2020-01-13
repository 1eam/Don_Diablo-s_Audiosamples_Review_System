package com.esther.dds.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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
    private String audioFile;


    //State LookupTable
    @ManyToOne
    @JoinColumn(name="state", referencedColumnName = "id") //decides wich name it presents in the 2hDB
    private State state;


//    public void addState (State state){
//        state = new State();
//    }



//    //PrettyTime Config:v3.0
//
//    public String getPrettyTime() {
//        PrettyTime pt = new PrettyTime();
//        return pt.format(convertToDateViaInstant(getCreationDate()));
//    }
//
//    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
//        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
//    }

}
