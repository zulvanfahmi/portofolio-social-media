package com.portofolio.socialMedia.services;

import java.util.Date;
import java.util.List;

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

    @Autowired
    private UserService userService;

    public void followUser(String usernameToFollow) {

        UserEntity user = userService.getCurrentUserEntity();

        if (user.getUsername().equals(usernameToFollow)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot follow yourself");
        }

        UserEntity following = userRepository.findByUsernameAndNotDeleted(usernameToFollow)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found: " + usernameToFollow));

        if (followRepository.existsByFollowerAndFollowing(
            user.getId_user(), 
            following.getId_user())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You already follow this user");
        }

        FollowEntity followEntity = new FollowEntity();
        followEntity.setFollower(user);
        followEntity.setFollowing(following);
        followEntity.setCreated_by(user.getId_user());

        followRepository.save(followEntity);
    }

    public void unfollowUser(String usernameToUnfollow) {

        UserEntity user = userService.getCurrentUserEntity();

        if (user.getUsername().equals(usernameToUnfollow)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot unfollow yourself");
        }

        UserEntity following = userRepository.findByUsernameAndNotDeleted(usernameToUnfollow)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found: " + usernameToUnfollow));

        FollowEntity followEntity = followRepository
        .findByIdFollowerAndIdFollowing(user.getId_user(), following.getId_user())
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not following this user"));

        followEntity.setDeleted_by(user.getId_user());
        followEntity.setDeleted_on(new Date());
        followEntity.setIs_delete(true);
        followRepository.save(followEntity);
    }

    public List<ListUserDTO> getListFollower(String username) {

        List<ListUserDTO> listFollower = followRepository.getListFollower(username);

        return listFollower;
    }
    
    public List<ListUserDTO> getListFollowing(String username) {

        List<ListUserDTO> listFollowing = followRepository.getListFollowing(username);

        return listFollowing;
    }

    public Boolean isFollowing(String targetUsername) {

        UserEntity user = userService.getCurrentUserEntity();

        UserEntity userTarget = userRepository.findByUsernameAndNotDeleted(targetUsername)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, targetUsername + " is not found"));

        return followRepository.existsByFollowerAndFollowing(user.getId_user(), userTarget.getId_user());
    }


}
