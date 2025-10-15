package com.portofolio.socialMedia.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.portofolio.socialMedia.dto.TweetDTO;
import com.portofolio.socialMedia.entities.TweetEntity;

import io.lettuce.core.dynamic.annotation.Param;

public interface TweetRepository extends JpaRepository<TweetEntity, Long> {

    @Query(nativeQuery = true,
    value = "SELECT \r\n" + //
            "t.id_tweet, \r\n" + //
            "t.content, \r\n" + //
            "t.image_urls, \r\n" + //
            "u.username, \r\n" + //
            "t.id_parent_tweet \r\n" + //
            "FROM tweet t\r\n" + //
            "inner join m_user u on t.id_user = u.id_user\r\n" + //
            "where t.id_tweet = 1\r\n" + //
            "and u.is_delete = false\r\n" + //
            "and t.is_delete = false")
    public Optional<TweetDTO> getTweetByIdAndNotDeleted(@Param("idTweet") Long idTweet);

    @Query(nativeQuery = true,
    value = "SELECT * FROM tweet where id_tweet = :idTweet and is_delete = false")
    public Optional<TweetEntity> getTweetReferenceById(@Param("idTweet") Long IdTweet);
}
