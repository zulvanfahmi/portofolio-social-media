package com.portofolio.socialMedia.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portofolio.socialMedia.dto.ListUserDTO;
import com.portofolio.socialMedia.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(nativeQuery = true, 
    value = "SELECT * FROM m_user WHERE username = :username and is_delete = false")
    public Optional<UserEntity> findByUsernameAndNotDeleted(@Param("username") String username);

    @Query(nativeQuery = true,
    value = "SELECT EXISTS (SELECT * FROM m_user WHERE username = :username and is_delete = false)")
    public Boolean isUsernameExist(@Param("username") String username);

    @Query(nativeQuery = true,
    value = "SELECT \r\n" + //
            "profile_image_url as profile_image_url,\r\n" + //
            "name as name,\r\n" + //
            "username as username, \r\n" + //
            "id_user as id_user_list\r\n" + //
            "FROM m_user\r\n" + //
            "WHERE is_delete = false\r\n" + //
            "AND (username ILIKE CONCAT('%', :keyword, '%') OR name ILIKE CONCAT('%', :keyword, '%'))"
            )
    public List<ListUserDTO> searchByNameOrUsername(@Param("keyword") String keyword);
    
}
