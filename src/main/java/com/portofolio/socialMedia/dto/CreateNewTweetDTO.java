package com.portofolio.socialMedia.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateNewTweetDTO {

    private String content;
    private String imageUrls;
    private Long id_user;
    private Long id_parentTweet;
    private Long created_by;

}