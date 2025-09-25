package com.portofolio.socialMedia.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.portofolio.socialMedia.dto.ListUserDTO;
import com.portofolio.socialMedia.entities.FollowEntity;
import com.portofolio.socialMedia.entities.UserEntity;
import com.portofolio.socialMedia.repositories.FollowRepository;
import com.portofolio.socialMedia.repositories.UserRepository;

@Service
public class FollowService {
    
    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    public void followUser(String username, String usernameToFollow) {

        if (username.equals(usernameToFollow)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "same user");
        }

        UserEntity follower = userRepository.findByUsernameAndNotDeleted(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "follower not found"));

        UserEntity following = userRepository.findByUsernameAndNotDeleted(usernameToFollow)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "user to follow is not found"));

        // Cek apakah sudah follow
        if (followRepository.existsByFollowerAndFollowing(
            follower.getId_user(), 
            following.getId_user())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "already following this user");
        }

        FollowEntity followEntity = new FollowEntity();
        followEntity.setFollower(follower);
        followEntity.setFollowing(following);
        followEntity.setCreated_by(follower.getId_user());

        followRepository.save(followEntity);

    }

    public void unfollowUser(String username, String usernameToUnfollow) {

        if (username.equals(usernameToUnfollow)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "same user");
        }

        UserEntity follower = userRepository.findByUsernameAndNotDeleted(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "follower is not found"));

        UserEntity following = userRepository.findByUsernameAndNotDeleted(usernameToUnfollow)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "user to follow is not found"));

        Optional<FollowEntity> optionalFollowEntity = followRepository.findByIdFollowerAndIdFollowing(follower.getId_user(), following.getId_user());
        
        FollowEntity followEntity = optionalFollowEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "user is not following"));

        followEntity.setDeleted_by(follower.getId_user());
        followEntity.setDeleted_on(new Date());
        followEntity.setIs_delete(true);
        followRepository.save(followEntity);

    }

    public List<ListUserDTO> getListFollower(String username) {

        List<ListUserDTO> listFollower = followRepository.getListFollower(username);

        if (listFollower.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "no data follower");
        }

        return listFollower;

    }
    
    public List<ListUserDTO> getListFollowing(String username) {

        List<ListUserDTO> listFollowing = followRepository.getListFollowing(username);

        if (listFollowing.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "no data following");
        }

        return listFollowing;

    }

    public Boolean isFollowedBy(String follower, String following) {

        UserEntity userFollower = userRepository.findByUsernameAndNotDeleted(follower)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "follower not exist"));

        UserEntity userFollowing = userRepository.findByUsernameAndNotDeleted(following)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "following not exist"));

        return followRepository.existsByFollowerAndFollowing(userFollower.getId_user(), userFollowing.getId_user());

    }


}
