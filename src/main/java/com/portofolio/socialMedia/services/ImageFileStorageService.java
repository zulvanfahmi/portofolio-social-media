package com.portofolio.socialMedia.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class ImageFileStorageService {

    public List<String> storeTweetImages(
            List<MultipartFile> files,
            Long id_user) {

        final String UPLOAD_DIR_TWEET = "uploads/images/tweet/";

        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {

            try {

                if (!file.getContentType().startsWith("image/")) {
                    throw new RuntimeException("Hanya file gambar yang diperbolehkan");
                }

                if (file.getSize() > 5 * 1024 * 1024) { // 5MB
                    throw new RuntimeException("File terlalu besar");
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

                throw new RuntimeException("Gagal upload file: " + file.getOriginalFilename(), e);

            }
        }

        return fileUrls;

    }
}
