package com.portofolio.socialMedia.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portofolio.socialMedia.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(nativeQuery = true, 
    value = "SELECT * FROM m_user WHERE username = :username and is_delete = false")
    public Optional<UserEntity> findByUsernameAndNotDeleted(@Param("username") String username);

    @Query(nativeQuery = true,
    value = "SELECT EXISTS (SELECT * FROM m_user WHERE username = :username and is_delete = false)")
    public Boolean isUsernameExist(@Param("username") String username);
    
}
