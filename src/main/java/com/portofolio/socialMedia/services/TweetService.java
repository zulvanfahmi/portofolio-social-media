package com.portofolio.socialMedia.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.portofolio.socialMedia.dto.TweetDTO;
import com.portofolio.socialMedia.entities.TweetEntity;
import com.portofolio.socialMedia.entities.UserEntity;
import com.portofolio.socialMedia.repositories.TweetRepository;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserService userService;

    public void createNewTweet(TweetDTO createNewTweetDTO) {

        UserEntity user = userService.getCurrentUserEntity();

        TweetEntity parentTweet;

        if (createNewTweetDTO.getId_parentTweet() != null) {
            parentTweet = tweetRepository.findById(createNewTweetDTO.getId_parentTweet())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent tweet not found"));
        } else {
            parentTweet = null;
        }

        TweetEntity newTweet = new TweetEntity();

        newTweet.setContent(createNewTweetDTO.getContent());
        newTweet.setImage_urls(createNewTweetDTO.getImage_urls());
        newTweet.setUser(user);
        newTweet.setParentTweet(parentTweet);
        newTweet.setCreated_by(user.getId_user());

        tweetRepository.save(newTweet);
    }

    public TweetDTO getTweetByIdAndNotDeleted(Long idTweet) {
        return tweetRepository
            .getTweetByIdAndNotDeleted(idTweet)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet not found"));
    }

    public void deleteById(Long idTweet) {
        UserEntity user = userService.getCurrentUserEntity();

        TweetEntity tweet = tweetRepository.getTweetReferenceById(idTweet)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "tweet not found"));
        tweet.setIs_delete(true);
        tweet.setDeleted_by(user.getId_user());
        tweet.setDeleted_on(new Date());
        
        tweetRepository.save(tweet);
    }
}
