package com.portofolio.socialMedia.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portofolio.socialMedia.dto.TweetDTO;
import com.portofolio.socialMedia.services.ImageFileStorageService;
import com.portofolio.socialMedia.services.TweetService;

@RestController
@RequestMapping("/api/tweet")
public class TweetRestController {
    
    @Autowired
    private TweetService tweetService;

    @Autowired
    private ImageFileStorageService imageFileStorageService;

    // /api/tweet/create
    @PostMapping("create")
    public ResponseEntity<String> createTweet(
        @ModelAttribute TweetDTO createNewTweetDTO,
        @RequestParam(value = "files", required = false) List<MultipartFile> files
        ) throws JsonProcessingException {

        if (files != null) {
            List<String> fileUrls = imageFileStorageService.storeTweetImages(files, createNewTweetDTO.getId_user());

            ObjectMapper objectMapper = new ObjectMapper();
            String listUrlImages = objectMapper.writeValueAsString(fileUrls);

            createNewTweetDTO.setImage_urls(listUrlImages);
        }

        tweetService.createNewTweet(createNewTweetDTO);

        return ResponseEntity.ok("Tweet created successfully");

    }

    //     Get Tweet by ID
    // GET /api/tweet/{tweetId}
    @GetMapping("/{idTweet}")
    public ResponseEntity<TweetDTO> getTweetById(
        @PathVariable("idTweet") Long idTweet
    ) {

        TweetDTO tweet = tweetService.getTweetByIdAndNotDeleted(idTweet);

        return ResponseEntity.ok(tweet);
    }

    // Delete Tweet
    // DELETE /api/tweets/{tweetId}
    @DeleteMapping("/{idTweet}")
    public ResponseEntity<String> deleteById(
        @PathVariable("idTweet") Long idTweet
    ) {

        tweetService.deleteById(idTweet);

        return ResponseEntity.ok("tweet deleted");

    }

}
