package com.portofolio.socialMedia.restcontrollers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.portofolio.socialMedia.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    // update name, bio
    @PutMapping("update/biodata")
    public ResponseEntity<String> updateUserBiodata(
        @RequestParam String name,
        @RequestParam String bio,
        @RequestParam String username
    ) {

        userService.updateUserBiodata(name, bio, username);

        return ResponseEntity.ok("Success update user biodata");

    }

    // update photo profile
    @PutMapping("update/photoprofile")
    public ResponseEntity<String> updatePhotoProfile(
        @RequestParam MultipartFile imagefile,
        @RequestParam String username
    ) {

        userService.updatePhotoProfile(username, imagefile);

        return ResponseEntity.ok("Success updata Photo Profile");

    }

    // check apakah username sudah dipakai
    @GetMapping("check/isUsernameExist")
    public ResponseEntity<Map<String, Object>> isUsernameExist(
        @RequestParam String oldUsername,
        @RequestParam String newUsername
    ) {

        Map<String, Object> response = new HashMap<>();

        if (userService.isUsernameExist(oldUsername, newUsername)) {
            
            response.put("message", "Username is already taken");
            response.put("isUsernameExist", true);

        } else {

            response.put("message", "Username is not taken");
            response.put("isUsernameExist", false);

        }

        return ResponseEntity.ok(response);

    }

    // update email

    // update password
    // @PutMapping("/update/password")
    // public ResponseEntity<String> updatePassword(
    //     @RequestParam String oldPassword,
    //     @RequestParam String newPassword
    // ) {

    //     userService.updatePassword(oldPassword, newPassword);

    //     return ResponseEntity.ok("Success updating password");
    // }

    // delete user

    // cari user berdasar nama dan username

    // regis user baru

}
