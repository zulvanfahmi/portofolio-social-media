package com.portofolio.socialMedia.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.portofolio.socialMedia.dto.ListUserDTO;
import com.portofolio.socialMedia.entities.FollowEntity;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

        @Query(nativeQuery = true, value = "select exists(select * from follows where id_follower = :id_user_follower and id_following = :id_user_following and is_delete = false)")
        public Boolean existsByFollowerAndFollowing(
                        @Param("id_user_follower") Long id_user_follower,
                        @Param("id_user_following") Long id_user_following);

        @Query(nativeQuery = true, value = "select * from follows where id_follower = :id_user_follower and id_following = :id_user_following and is_delete = false")
        public Optional<FollowEntity> findByIdFollowerAndIdFollowing(
                        @Param("id_user_follower") Long id_user_follower,
                        @Param("id_user_following") Long id_user_following);

        @Query(nativeQuery = true, value = "select\r\n" + //
                        "u.profile_image_url as profile_image_url,\r\n" + //
                        "u.username as username,\r\n" + //
                        "u.name as name,\r\n" + //
                        "f.id_follower as id_user_list\r\n" + //
                        "from m_user u\r\n" + //
                        "inner join follows f on u.id_user = f.id_follower \r\n" + //
                        "where u.is_delete = false\r\n" + //
                        "and f.is_delete = false\r\n" + //
                        "and f.id_following = (select id_user from m_user where username = :username and is_delete = false)")
        public List<ListUserDTO> getListFollower(
                        @Param("username") String username);

        @Query(nativeQuery = true, value = "select\r\n" + //
                        "u.profile_image_url as profile_image_url,\r\n" + //
                        "u.username as username,\r\n" + //
                        "u.name as name,\r\n" + //
                        "f.id_following as id_user_list\r\n" + //
                        "from m_user u\r\n" + //
                        "inner join follows f on u.id_user = f.id_following \r\n" + //
                        "where u.is_delete = false\r\n" + //
                        "and f.is_delete = false\r\n" + //
                        "and f.id_follower = (select id_user from m_user where username = :username and is_delete = false)")
        public List<ListUserDTO> getListFollowing(
                        @Param("username") String username);

}
