package com.esther.dds.service;


import com.esther.dds.Globals.Globals;
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

    public Path findThisMachinesPath(){
        Path findCurrentLocation;
        Path thisMachinesPath = null;

        switch(Globals.OS) {
            case "Windows":
                //find the root of the projectfolder & the path of the target location
                findCurrentLocation = Paths.get(".");
                thisMachinesPath = findCurrentLocation.toAbsolutePath();
                Globals.targetPath = "\\target\\classes\\static\\uploads\\audioFiles\\";
                break;
            case "Linux Ubuntu":
                //find the root of the projectfolder & the path of the target location
                findCurrentLocation = Path.of(Paths.get("") + "Demo Drop System by Esther A M");
                thisMachinesPath = findCurrentLocation.toAbsolutePath();
                Globals.targetPath = "/target/classes/static/uploads/audioFiles/";
                break;
        }
        return thisMachinesPath;
    }

    public void saveAudioFile(Demo demo, MultipartFile audioFile) throws Exception{
        Path thisMachinesPath = findThisMachinesPath();
        //generate & set the "audioFiles" field in the database
        demo.setAudioFile("/uploads/audioFiles/" + audioFile.getOriginalFilename());

        //actually write the file to disk
        byte[] bytes = audioFile.getBytes();
        Path fullPath = Paths.get(thisMachinesPath + Globals.targetPath + audioFile.getOriginalFilename());
        Files.write(fullPath, bytes);
    }
}