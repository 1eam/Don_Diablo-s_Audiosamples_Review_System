package com.esther.dds.domain;

import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import java.net.URI;
import java.net.URISyntaxException;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


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
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="State")
    private State state;



//    //PrettyTime Config
//    public String getDomainName() throws URISyntaxException {
//        URI uri = new URI(this.audioFile);
//        String domain = uri.getHost();
//        return domain.startsWith("www.") ? domain.substring(4) : domain;
//    } //delte method?
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
