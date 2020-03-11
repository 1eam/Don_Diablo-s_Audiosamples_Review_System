package com.esther.dds.service;


import com.esther.dds.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ProfileImageService {
    private final Logger logger = LoggerFactory.getLogger(ProfileImageService.class);



    public void saveProfileImage(User user, MultipartFile profileImage) throws Exception{
        /* U hoeft verder niks te doen, Maar Werkt demo uploaden niet? Verander dan hieronder de absolute PATH
        naar de locatie waar deze map zich bevindt: "Demo Drop System by Esther A M" */
        //String uwMap = "D:\\1_Novi_Examenproject\\3. Frontend + Backend";


        //find the root of this folder "Demo Drop System by Esther A M"
        Path findCurrentLocation = Paths.get(".");

        //find the path to this location
        Path yourPath = findCurrentLocation.toAbsolutePath();

        //generate & set the "ProfileImage" field in the database
        user.setProfileImage("/uploads/profileimagefiles/" + profileImage.getOriginalFilename());

        //actually write the file to disk
        byte[] bytes = profileImage.getBytes();
        Path pathToRuntimePreview = Paths.get(yourPath + "\\target\\classes\\static\\uploads\\profileimagefiles\\" + profileImage.getOriginalFilename());
        Path pathToSaveCopyOnDisk = Paths.get(yourPath + "\\src\\main\\resources\\static\\uploads\\profileimagefiles\\" + profileImage.getOriginalFilename());

        Files.write(pathToRuntimePreview, bytes);
        Files.write(pathToSaveCopyOnDisk, bytes);



//        System.out.println(path.toAbsolutePath());
    }
}