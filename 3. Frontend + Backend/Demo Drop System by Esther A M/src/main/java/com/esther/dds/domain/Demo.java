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
    private String audioFile;


    //State LookupTable
    @ManyToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="state", referencedColumnName = "id")
    private State state;



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
