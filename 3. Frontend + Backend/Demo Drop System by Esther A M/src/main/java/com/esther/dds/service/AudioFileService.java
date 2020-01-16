package com.esther.dds.service;


import com.esther.dds.domain.Demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AudioFileService {
    private final Logger logger = LoggerFactory.getLogger(AudioFileService.class);



    public void saveAudio(Demo demo, MultipartFile audioFile) throws Exception{
        /* U hoeft verder niks te doen, Maar Werkt demo uploaden niet? Verander dan hieronder de absolute PATH
        naar de locatie waar deze map zich bevindt: "Demo Drop System by Esther A M" */
        //String uwMap = "D:\\1_Novi_Examenproject\\3. Frontend + Backend";

        //vind de root van deze map: "Demo Drop System by Esther A M"
        Path findCurrentLocation = Paths.get(".");
        //vind de volledige mappenstruktuur naar deze locatie
        Path uwMappenStruktuur = findCurrentLocation.toAbsolutePath();

        //update de "AudioFile" veld in database: Locatie: string"
        demo.setAudioFile("/audio/" + audioFile.getOriginalFilename());
        byte[] bytes = audioFile.getBytes();
        Path path = Paths.get(uwMappenStruktuur + "\\src\\main\\resources\\static\\audio\\" + audioFile.getOriginalFilename());
        Files.write(path, bytes);



//        System.out.println(path.toAbsolutePath());
    }
}
=======



