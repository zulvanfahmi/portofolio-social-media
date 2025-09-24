package com.portofolio.socialMedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portofolio.socialMedia.entities.TweetEntity;

public interface TweetRepository extends JpaRepository<TweetEntity, Long> {
    
}
