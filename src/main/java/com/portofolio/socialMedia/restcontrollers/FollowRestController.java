package com.portofolio.socialMedia.restControllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portofolio.socialMedia.dto.ListUserDTO;
import com.portofolio.socialMedia.services.FollowService;
import com.portofolio.socialMedia.utils.ApiResponseWrapper;

@RestController
@RequestMapping("/api")
public class FollowRestController {

    @Autowired
    private FollowService followService;
    
    // Follow user
    @PostMapping("/follow/{usernameToFollow}")
    public ResponseEntity<ApiResponseWrapper<String>> followUser(
        @PathVariable String usernameToFollow
    ) {
        followService.followUser(usernameToFollow);
        return ResponseEntity.ok(
            new ApiResponseWrapper<>(
                "Success following user " + usernameToFollow, 
                null));
    }

    // Unfollow user
    @DeleteMapping("/unfollow/{usernameToUnfollow}")
    public ResponseEntity<ApiResponseWrapper<String>> unfollowUser(
        @PathVariable String usernameToUnfollow
    ) {
        followService.unfollowUser(usernameToUnfollow);
        return ResponseEntity.ok( 
            new ApiResponseWrapper<>(
                "Unfollowed user " + usernameToUnfollow,
                null)
            );
    }

    // get /api/follow/{username}/followers
    @GetMapping("/{username}/followers")
    public ResponseEntity<ApiResponseWrapper<Map<String, Object>>> getListFollower(
        @PathVariable String username
    ) {
        List<ListUserDTO> listUserData = followService.getListFollower(username);

        if (listUserData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(new ApiResponseWrapper<>("No follower found", null));
        }

        Map<String, Object> response = new HashMap<>(Map.of(
            "countData", listUserData.size(),
            "listData", listUserData
        ));

        return ResponseEntity.ok(
            new ApiResponseWrapper<>("Success retrieve list follower", response));
    }

    // get /api/follow/{username}/following
    @GetMapping("/{username}/following")
    public ResponseEntity<ApiResponseWrapper<Map<String, Object>>> getListFollowing(
        @PathVariable String username
    ) {
        List<ListUserDTO> listUserData = followService.getListFollowing(username);
        
        if (listUserData.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(new ApiResponseWrapper<>("No following found", null));
        }

        Map<String, Object> response = new HashMap<>(Map.of(
            "countData", listUserData.size(),
            "listData", listUserData
        ));

        return ResponseEntity.ok(
            new ApiResponseWrapper<>("Success retrieve list following", response));
    }

    // GET /api/follow/check?follower={username}&following={username}
    @GetMapping("/user-is-following/{targetUsername}")
    public ResponseEntity<ApiResponseWrapper<Boolean>> isFollowing(
        @PathVariable String targetUsername
    ) {
        Boolean checkResult = followService.isFollowing(targetUsername);

        return ResponseEntity.ok(
            new ApiResponseWrapper<>("Success checking", checkResult));
    }
}
