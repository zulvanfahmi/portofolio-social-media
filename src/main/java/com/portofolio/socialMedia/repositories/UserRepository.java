package com.portofolio.socialMedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portofolio.socialMedia.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
}
