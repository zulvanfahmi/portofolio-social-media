package com.portofolio.socialMedia.services;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.portofolio.socialMedia.entities.UserEntity;
import com.portofolio.socialMedia.repositories.UserRepository;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    public void updateUserBiodata(String name, String bio, String username) {

        Optional<UserEntity> userEntity = userRepository.findByUsernameAndNotDeleted(username);

        if (userEntity.isEmpty()) {
            
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user not found");

        }

        UserEntity userUpdated = userEntity.get();
        userUpdated.setBio(bio);
        userUpdated.setName(name);
        userUpdated.setModified_by(userUpdated.getId_user());
        userRepository.save(userUpdated);

    }

    @SuppressWarnings("null")
    public void updatePhotoProfile(String username, MultipartFile imageFile) {

        Optional<UserEntity> userEntity = userRepository.findByUsernameAndNotDeleted(username);

        if (userEntity.isEmpty()) {
            
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user not found");

        }

        UserEntity userUpdated = userEntity.get();

        final String UPLOAD_DIR_PHOTOPROFILE = "uploads/images/photo_profile/";

        String fileUrls;

            try {

                if (!imageFile.getContentType().startsWith("image/")) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Hanya file gambar yang diperbolehkan");
                }

                if (imageFile.getSize() > 5 * 1024 * 1024) { // 5MB
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "size file terlalu besar");
                }

                File directory = new File(UPLOAD_DIR_PHOTOPROFILE);
                if (!directory.exists())directory.mkdirs();

                String fileName = Long.toString(userUpdated.getId_user()) + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID()
                        + "_" + imageFile.getOriginalFilename();
                
                File outputFile = new File(UPLOAD_DIR_PHOTOPROFILE + fileName);

                Thumbnails.of(imageFile.getInputStream())
                        .size(1080, 1080)
                        .outputQuality(0.8)
                        .toFile(outputFile);

                fileUrls = "/" + UPLOAD_DIR_PHOTOPROFILE + fileName;

            } catch (IOException e) {

                throw new ResponseStatusException(HttpStatus.CONFLICT,  "Gagal upload file: " + imageFile.getOriginalFilename() +"---"+ e.getMessage());

            }

        userUpdated.setProfile_image_url(fileUrls);
        userUpdated.setModified_by(userUpdated.getId_user());
        userRepository.save(userUpdated);

    }

    public Boolean isUsernameExist(
        String oldUsername,
        String newUsername
    ){

        Optional<UserEntity> userEntity = userRepository.findByUsernameAndNotDeleted(oldUsername);

        if (userEntity.isEmpty()) {
            
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user not found");

        }

        UserEntity user = userEntity.get();

        if (user.getUsername().equals(newUsername)) {
            
            throw new ResponseStatusException(HttpStatus.CONFLICT, "usernames are same");

        }

        if(userRepository.isUsernameExist(newUsername)){

            return true;

        }

        return false;

    }

    // public void updatePassword(String username, String oldPassword, String newPassword) {

    //     Optional<UserEntity> userEntity = userRepository.findByUsernameAndNotDeleted(username);

    //     if (userEntity.isEmpty()) {
            
    //         throw new ResponseStatusException(HttpStatus.CONFLICT, "user not found");

    //     }

    //     UserEntity user = userEntity.get();

    //     if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
    //         throw new RuntimeException("Old password is incorrect");
    //     }

    // }

}
