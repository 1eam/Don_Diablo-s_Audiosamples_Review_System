package com.esther.dds.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Please enter a title")
    private String title;

    @NonNull
    @NotEmpty(message = "Description is required")
    @Column(length = 350)
    private String description;

    @NonNull
//   @NotEmpty(message = "Select a file")
    private String audioFile;


    //State LookupTable
    @ManyToOne
    @JoinColumn(name="state", referencedColumnName = "id") //decides wich name it presents in the 2hDB
    private State state;

    @ManyToOne
    @JoinColumn(name="uploadedBy", referencedColumnName = "id")
    private User user;


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
