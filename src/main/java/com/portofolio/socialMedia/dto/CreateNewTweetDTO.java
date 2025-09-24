package com.portofolio.socialMedia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNewTweetDTO {

    private String content;
    private String mediaUrl;
    private Long id_user;
    private Long id_parentTweet;

}