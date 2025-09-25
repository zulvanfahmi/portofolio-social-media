package com.portofolio.socialMedia.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class ImageFileStorageService {

    @SuppressWarnings("null")
    public List<String> storeTweetImages(
            List<MultipartFile> files,
            Long id_user) {

        final String UPLOAD_DIR_TWEET = "uploads/images/tweet/";

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {

            try {

                if (!file.getContentType().startsWith("image/")) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Hanya file gambar yang diperbolehkan");
                }

                if (file.getSize() > 5 * 1024 * 1024) { // 5MB
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "size file terlalu besar");
                }

                File directory = new File(UPLOAD_DIR_TWEET);
                if (!directory.exists())
                    directory.mkdirs();

                String fileName = Long.toString(id_user) + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID()
                        + "_" + file.getOriginalFilename();
                File outputFile = new File(UPLOAD_DIR_TWEET + fileName);

                Thumbnails.of(file.getInputStream())
                        .size(1080, 1080)
                        .outputQuality(0.8)
                        .toFile(outputFile);

                fileUrls.add("/" + UPLOAD_DIR_TWEET + fileName);

            } catch (IOException e) {

                throw new ResponseStatusException(HttpStatus.CONFLICT,  "Gagal upload file: " + file.getOriginalFilename() +"---"+ e.getMessage());

            }
        }

        return fileUrls;

    }
}
