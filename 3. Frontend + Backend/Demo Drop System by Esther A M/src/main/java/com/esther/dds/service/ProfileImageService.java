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
import com.esther.dds.domain.User;
/**
 * Before you run the application you should set the path to the location where the profileimages will be stored
 * the Paths.get method works slightly different on Windows and Ubuntu
 * If you use one of these you can simply set the OS variable in the findAbsolutePath method
 */

@Service
public class ProfileImageService {
    private final Logger logger = LoggerFactory.getLogger(ProfileImageService.class);

    public Path findThisMachinesPath(){
        Path findCurrentLocation;
        Path thisMachinesPath = null;

        switch(Globals.OS) {
            case "Windows":
                //find the root of the projectfolder & the path of the target location
                findCurrentLocation = Paths.get(".");
                thisMachinesPath = findCurrentLocation.toAbsolutePath();
                Globals.targetPath = "\\target\\classes\\static\\uploads\\profileImageFiles\\";
                break;
            case "Linux Ubuntu":
                //find the root of the projectfolder & the path of the target location
                findCurrentLocation = Path.of(Paths.get("") + "Demo Drop System by Esther A M");
                thisMachinesPath = findCurrentLocation.toAbsolutePath();
                Globals.targetPath = "/target/classes/static/uploads/profileImageFiles/";
                break;
        }
        return thisMachinesPath;
    }

    public void saveProfileImage(User user, MultipartFile profileImage) throws IOException{
        Path thisMachinesPath = findThisMachinesPath();
        //generate & set the "ProfileImage" field in the database
        user.setProfileImage("/uploads/profileImageFiles/" + profileImage.getOriginalFilename());

        //actually write the file to disk
        byte[] bytes = profileImage.getBytes();
        Path fullPath = Paths.get(thisMachinesPath + Globals.targetPath + profileImage.getOriginalFilename());
        Files.write(fullPath, bytes);
    }
}