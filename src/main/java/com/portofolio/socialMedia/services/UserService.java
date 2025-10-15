package com.portofolio.socialMedia.services;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.portofolio.socialMedia.dto.CreateNewUserDTO;
import com.portofolio.socialMedia.dto.ListUserDTO;
import com.portofolio.socialMedia.entities.UserEntity;
import com.portofolio.socialMedia.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${photoprofile.file.directory}")
    private String UPLOAD_DIR_PHOTOPROFILE;

    @Value("${image.file.size}")
    private Long PHOTOPROFILE_SIZE;

    public UserEntity getCurrentUserEntity() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user = userRepository.findByUsernameAndNotDeleted(username).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));

        return user;
    }
    
    public void updateUserBiodata(String name, String bio) {

        UserEntity user = getCurrentUserEntity();

        user.setBio(bio);
        user.setName(name);
        user.setModified_by(user.getId_user());
        userRepository.save(user);
    }

    @SuppressWarnings("null")
    public void updatePhotoProfile(MultipartFile imageFile) {

        UserEntity user = getCurrentUserEntity();

        String fileUrls;

            try {

                String contentType = imageFile.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only Image File Allowed");
                }

                String originalName = imageFile.getOriginalFilename();
                if (originalName == null || 
                !(originalName.endsWith(".jpg") || originalName.endsWith(".jpeg") || originalName.endsWith(".png"))) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPG, JPEG or PNG is Allowed");
                }

                if (imageFile.getSize() > PHOTOPROFILE_SIZE) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File Size is too big");
                }

                File directory = new File(UPLOAD_DIR_PHOTOPROFILE);
                if (!directory.exists())directory.mkdirs();

                String fileName = 
                    Long.toString(user.getId_user()) 
                    + "_" + System.currentTimeMillis() 
                    + "_" + UUID.randomUUID()
                    + "_" + imageFile.getOriginalFilename();
                
                File outputFile = new File(UPLOAD_DIR_PHOTOPROFILE + fileName);

                Thumbnails.of(imageFile.getInputStream())
                        .size(1080, 1080)
                        .outputQuality(0.8)
                        .toFile(outputFile);

                fileUrls = "/" + UPLOAD_DIR_PHOTOPROFILE + fileName;

            } catch (IOException e) {

                log.error("Failed Upload File: {}", e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,  "Failed Upload File");
            }
            
        user.setProfile_image_url(fileUrls);
        user.setModified_by(user.getId_user());
        userRepository.save(user);
    }

    public Boolean isUsernameExist(String newUsername){

        UserEntity user = getCurrentUserEntity();

        if (user.getUsername().equals(newUsername)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new username must be different from current one");

        Boolean isUsernameExist = userRepository.isUsernameExist(newUsername);

        if (isUsernameExist) throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is Already Used");

        return isUsernameExist;
    }

    public void updatePassword(String oldPassword, String newPassword) {

        UserEntity user = getCurrentUserEntity();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"Old Password is Incorrect");

        if (passwordEncoder.matches(newPassword, user.getPassword())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be different from old password");

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        user.setModified_by(user.getId_user());
        userRepository.save(user);
    }

    public Boolean updateEmail(String email) {

        UserEntity user = getCurrentUserEntity();

        user.setEmail(email);
        user.setModified_by(user.getId_user());

        userRepository.save(user);

        return true;
    }

    public List<ListUserDTO> searchByNameOrUsername(String keyword) {

        List<ListUserDTO> listUser =  userRepository.searchByNameOrUsername(keyword);

        return listUser;
    }

    public void createNewUser(CreateNewUserDTO newUserDTO) {

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

        UserEntity user = getCurrentUserEntity();

        user.setIs_delete(true);
        user.setDeleted_by(user.getId_user());
        user.setDeleted_on(new Date());

        userRepository.save(user);
    }

}
