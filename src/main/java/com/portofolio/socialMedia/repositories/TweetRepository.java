package com.portofolio.socialMedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portofolio.socialMedia.dto.TweetDTO;
import com.portofolio.socialMedia.entities.TweetEntity;

import io.lettuce.core.dynamic.annotation.Param;

public interface TweetRepository extends JpaRepository<TweetEntity, Long> {

    @Query(nativeQuery = true,
    value = "SELECT content, image_urls, id_user, id_parentTweet, created_by FROM tweet where id_tweet = :idTweet and is_delete = false")
    public TweetDTO getTweetByIdAndNotDeleted(@Param("idTweet") Long idTweet);
    
}
