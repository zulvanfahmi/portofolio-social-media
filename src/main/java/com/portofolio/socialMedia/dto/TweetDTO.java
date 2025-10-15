package com.portofolio.socialMedia.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TweetDTO {

    private Long id_tweet;
    private String content;
    private String image_urls;
    private String username;
    private Long id_parentTweet;

}