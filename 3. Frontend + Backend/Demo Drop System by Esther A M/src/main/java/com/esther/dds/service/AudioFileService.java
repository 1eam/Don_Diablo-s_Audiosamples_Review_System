package com.esther.dds.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.esther.dds.Globals.Globals;
import com.esther.dds.domain.Demo;
@Service
public class AudioFileService {
    private final Logger logger = LoggerFactory.getLogger(AudioFileService.class);

    public Path findThisMachinesPath(){
        Path findCurrentLocation;
        Path thisMachinesPath = null;

        // Probably not needed :). The Audiofiles (and profileImageFiles) are part of the application? Then they can be accessed by ClassLoader().getResourceAsStream(...) 
        // IF not (which is the case) define some setting in a property file, which specify the locations (Properties)
        // With this you do not need the Globals and this OS.specific code
        switch(Globals.OS) {
            case "Windows":
                //find the root of the projectfolder & the path of the target location
                findCurrentLocation = Paths.get(".");
                thisMachinesPath = findCurrentLocation.toAbsolutePath();
                // this is a maven decided location, you can reach this by using ClassLoader().getResourceAsStream("static/uploads/audioFiles")
                // but this is not what you want. So this has be some setting.
                
                // Path is designed to solve the issue of / or \. Use Paths.get(somePathString, "static", "uploads" , "audioFiles")
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

    public void saveAudioFile(Demo demo, MultipartFile audioFile) throws IOException{
        Path thisMachinesPath = findThisMachinesPath();
        //generate & set the "audioFiles" field in the database
        demo.setAudioFile("/uploads/audioFiles/" + audioFile.getOriginalFilename());

        //actually write the file to disk
        byte[] bytes = audioFile.getBytes();
        Path fullPath = Paths.get(thisMachinesPath + Globals.targetPath + audioFile.getOriginalFilename());
        Files.write(fullPath, bytes);
    }
}