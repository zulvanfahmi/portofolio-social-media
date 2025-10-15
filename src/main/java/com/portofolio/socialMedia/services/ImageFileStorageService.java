package com.portofolio.socialMedia.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.portofolio.socialMedia.entities.UserEntity;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

@Slf4j
@Service
public class ImageFileStorageService {

    @Autowired
    private UserService userService;

    @Value("${tweeet.file.directory}")
    private String UPLOAD_DIR_TWEET;

    @Value("${image.file.size}")
    private Long TWEET_IMAGE_SIZE;

    @SuppressWarnings("null")
    public List<String> storeTweetImages(
            List<MultipartFile> multipleImageFiles) {

        UserEntity user = userService.getCurrentUserEntity();

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile imageFile : multipleImageFiles) {

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

                if (imageFile.getSize() > TWEET_IMAGE_SIZE) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File Size is too big");
                }

                File directory = new File(UPLOAD_DIR_TWEET);
                if (!directory.exists())directory.mkdirs();

                String fileName = 
                    Long.toString(user.getId_user())
                    + "_" + System.currentTimeMillis()
                    + "_" + UUID.randomUUID()
                    + "_" + imageFile.getOriginalFilename();

                File outputFile = new File(UPLOAD_DIR_TWEET + fileName);

                Thumbnails.of(imageFile.getInputStream())
                        .size(1080, 1080)
                        .outputQuality(0.8)
                        .toFile(outputFile);

                fileUrls.add("/" + UPLOAD_DIR_TWEET + fileName);

            } catch (IOException e) {
                log.error("Failed Upload File: {}", e.getMessage(), e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,  "Failed Upload File");
            }
        }
        return fileUrls;
    }
}
