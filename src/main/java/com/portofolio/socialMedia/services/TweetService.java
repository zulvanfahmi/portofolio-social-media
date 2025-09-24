package com.portofolio.socialMedia.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portofolio.socialMedia.dto.CreateNewTweetDTO;
import com.portofolio.socialMedia.entities.TweetEntity;
import com.portofolio.socialMedia.entities.UserEntity;
import com.portofolio.socialMedia.repositories.TweetRepository;
import com.portofolio.socialMedia.repositories.UserRepository;

@Service
public class TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private UserRepository userRepository;

    public void createNewTweet(CreateNewTweetDTO createNewTweetDTO) {

        UserEntity userEntity = userRepository.findById(createNewTweetDTO.getId_user())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TweetEntity parentTweet = null;
        if (createNewTweetDTO.getId_parentTweet() != null) {
            parentTweet = tweetRepository.findById(createNewTweetDTO.getId_parentTweet())
                    .orElseThrow(() -> new RuntimeException("Parent tweet not found"));
        }

        TweetEntity newTweet = new TweetEntity();

        newTweet.setContent(createNewTweetDTO.getContent());
        newTweet.setMediaUrl(createNewTweetDTO.getMediaUrl());
        newTweet.setUser(userEntity);
        newTweet.setParentTweet(parentTweet);

        tweetRepository.save(newTweet);

    }

}
