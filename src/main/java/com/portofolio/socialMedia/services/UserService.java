package com.portofolio.socialMedia.services;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.portofolio.socialMedia.dto.CreateNewUserDTO;
import com.portofolio.socialMedia.dto.ListUserDTO;
import com.portofolio.socialMedia.entities.UserEntity;
import com.portofolio.socialMedia.repositories.UserRepository;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
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

    public void updatePassword(String oldPassword, String newPassword) {

        // 1. Dapatkan objek autentikasi dari konteks
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 2. Cek dan casting objek principal
        String username = null;
        if (principal instanceof UserDetails) {
            username =  ((UserDetails) principal).getUsername();
        }

        Optional<UserEntity> userEntity = userRepository.findByUsernameAndNotDeleted(username);

        if (userEntity.isEmpty()) {
            
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user not found");

        }

        UserEntity user = userEntity.get();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        user.setModified_by(user.getId_user());
        userRepository.save(user);

    }

    public Boolean updateEmail(String email) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = null;
        if (principal instanceof UserDetails) {
            username =  ((UserDetails) principal).getUsername();
        }

        Optional<UserEntity> userEntity = userRepository.findByUsernameAndNotDeleted(username);

        if (userEntity.isEmpty()) {
            
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user not found");

        }

        UserEntity user = userEntity.get();
        user.setEmail(email);
        user.setModified_by(user.getId_user());
        user.setModified_on(new Date());

        userRepository.save(user);

        return true;

    }

    public List<ListUserDTO> searchByNameOrUsername(String keyword) {

        List<ListUserDTO> listUser =  userRepository.searchByNameOrUsername(keyword);

        return listUser;

    }

    public void createNewUser(CreateNewUserDTO newUserDTO) {

        System.out.println(newUserDTO.toString());

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(newUserDTO.getUsername());
        userEntity.setEmail(newUserDTO.getEmail());
        userEntity.setPassword(passwordEncoder.encode(newUserDTO.getPassword()));
        userEntity.setName(newUserDTO.getName());
        userEntity.setRole("USER");
        userEntity.setCreated_by(0L);

        userRepository.save(userEntity);

    }

    public void deleteUser() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = null;
        if (principal instanceof UserDetails) {
            username =  ((UserDetails) principal).getUsername();
        }

        Optional<UserEntity> userEntity = userRepository.findByUsernameAndNotDeleted(username);

        if (userEntity.isEmpty()) {
            
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user not found");

        }

        UserEntity user = userEntity.get();
        user.setIs_delete(true);
        user.setDeleted_by(user.getId_user());
        user.setDeleted_on(new Date());

        userRepository.save(user);
        

    }

}
