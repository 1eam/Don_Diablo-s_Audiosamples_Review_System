package com.esther.dds.service;


import com.esther.dds.domain.Demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class AudioFileService {
    private final Logger logger = LoggerFactory.getLogger(AudioFileService.class);


    /* Werkt demo uploaden niet? Verander dan hieronder de absolute PATH
    naar de locatie waar deze map zich bevindt: "Demo Drop System by Esther A M" */
    //String yourPath = "D:\\1_Novi_Examenproject\\3. Frontend + Backend";
    public void saveAudio(Demo demo, MultipartFile audioFile) throws Exception{

        //find the root of this folder "Demo Drop System by Esther A M"
        Path findCurrentLocation = Paths.get(".");

        //find the path to this location
        Path yourPath = findCurrentLocation.toAbsolutePath();

        //generate & set the "AudioFile" field in the database
        demo.setAudioFile("/uploads/audiofiles/" + audioFile.getOriginalFilename());

        byte[] bytes = audioFile.getBytes();
        Path path = Paths.get(yourPath + "\\src\\main\\resources\\static\\uploads\\audiofiles\\" + audioFile.getOriginalFilename());
        Files.write(path, bytes);

//        System.out.println(path.toAbsolutePath());
    }
}

