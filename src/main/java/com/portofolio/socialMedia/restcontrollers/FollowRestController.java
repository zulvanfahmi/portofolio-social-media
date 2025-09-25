package com.portofolio.socialMedia.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portofolio.socialMedia.dto.ListUserDTO;
import com.portofolio.socialMedia.services.FollowService;

@RestController
@RequestMapping("/api")
public class FollowRestController {

    @Autowired
    private FollowService followService;
    
    // Follow user
    @PostMapping("/{username}/follow/{usernameToFollow}")
    public ResponseEntity<String> followUser(
        @PathVariable String username,
        @PathVariable String usernameToFollow
    ) {
        followService.followUser(username, usernameToFollow);
        return ResponseEntity.ok("Followed user " + usernameToFollow);
    }

    // Unfollow user
    @DeleteMapping("/{username}/unfollow/{usernameToUnfollow}")
    public ResponseEntity<String> unfollowUser(
        @PathVariable String username,
        @PathVariable String usernameToUnfollow
    ) {
        followService.unfollowUser(username, usernameToUnfollow);
        return ResponseEntity.ok("Unfollowed user " + usernameToUnfollow);
    }

    // Ambil daftar followers
    // get /api/follow/{username}/followers
    @GetMapping("/{username}/followers")
    public ResponseEntity<List<ListUserDTO>> getListFollower(
        @PathVariable String username
    ) {
        List<ListUserDTO> listUserData = followService.getListFollower(username);
        return ResponseEntity.ok(listUserData);
    }

    // Ambil daftar following
    // get /api/follow/{username}/following
    @GetMapping("/{username}/following")
    public ResponseEntity<List<ListUserDTO>> getListFollowing(
        @PathVariable String username
    ) {
        List<ListUserDTO> listUserData = followService.getListFollowing(username);
        return ResponseEntity.ok(listUserData);
    }

    // Cek apakah user A sudah follow user B
    // GET /api/follow/check?follower={username}&following={username}
    @GetMapping("/follow/check")
    public ResponseEntity<Boolean> isFollowdBy(
        @RequestParam String follower,
        @RequestParam String following
    ) {

        Boolean checkResult = followService.isFollowedBy(follower, following);
        return ResponseEntity.ok(checkResult);

    }


}
