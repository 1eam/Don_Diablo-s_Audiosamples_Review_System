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

        //vind de root van deze map: "Demo Drop System by Esther A M"
        Path findCurrentLocation = Paths.get(".");
        //vind de volledige mappenstruktuur naar deze locatie
        Path uwMappenStruktuur = findCurrentLocation.toAbsolutePath();

        //update de "AudioFile" veld in database: Locatie: string"
        user.setProfileImage("/audio/" + profileImage.getOriginalFilename());
        byte[] bytes = profileImage.getBytes();
        Path path = Paths.get(uwMappenStruktuur + "\\src\\main\\resources\\static\\audio\\" + profileImage.getOriginalFilename());
        Files.write(path, bytes);



//        System.out.println(path.toAbsolutePath());
    }
}