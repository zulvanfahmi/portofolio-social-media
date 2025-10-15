package com.portofolio.socialMedia.restControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portofolio.socialMedia.dto.TweetDTO;
import com.portofolio.socialMedia.services.ImageFileStorageService;
import com.portofolio.socialMedia.services.TweetService;
import com.portofolio.socialMedia.utils.ApiResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/tweet")
public class TweetRestController {
    
    @Autowired
    private TweetService tweetService;

    @Autowired
    private ImageFileStorageService imageFileStorageService;

    // /api/tweet/create
    @PostMapping("/create")
    public ResponseEntity<ApiResponseWrapper<String>> createTweet(
        @ModelAttribute TweetDTO createNewTweetDTO,
        @RequestParam(required = false) List<MultipartFile> files
        ) {

        if ((files == null || files.isEmpty() || files.stream().allMatch(MultipartFile::isEmpty)) && createNewTweetDTO.getContent().isBlank()) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseWrapper<>(
                    "Can't make tweet with no content and images", 
                    null));
        }
        
        if (files != null) {
            
            List<String> fileUrls = imageFileStorageService.storeTweetImages(files);

            try {

                ObjectMapper objectMapper = new ObjectMapper();
                String listUrlImages = objectMapper.writeValueAsString(fileUrls);
                createNewTweetDTO.setImage_urls(listUrlImages);

            } catch (Exception e) {

                log.error("Failed convert json to string: {}", e.getMessage(), e);;
                return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseWrapper<>(
                    "Error saving images, please contact administrator", 
                    null));
            }
        }

        tweetService.createNewTweet(createNewTweetDTO);

        return ResponseEntity.ok(
            new ApiResponseWrapper<>(
                "Tweet created successfully", 
                null));
    }

    // Get Tweet by ID
    // GET /api/tweet/{tweetId}
    @GetMapping("/{idTweet}")
    public ResponseEntity<ApiResponseWrapper<TweetDTO>> getTweetById(
        @PathVariable("idTweet") Long idTweet
    ) {
        TweetDTO tweet = tweetService.getTweetByIdAndNotDeleted(idTweet);

        return ResponseEntity.ok(
            new ApiResponseWrapper<>( 
                "Success retrieve tweet data", 
                tweet));
    }

    // Delete Tweet
    // DELETE /api/tweets/{tweetId}
    @DeleteMapping("/{idTweet}")
    public ResponseEntity<ApiResponseWrapper<String>> deleteById(
        @PathVariable("idTweet") Long idTweet
    ) {
        tweetService.deleteById(idTweet);

        return ResponseEntity.ok(
            new ApiResponseWrapper<>("tweet deleted", null));
    }
}
