package com.portofolio.socialMedia.restControllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.portofolio.socialMedia.dto.CreateNewUserDTO;
import com.portofolio.socialMedia.dto.ListUserDTO;
import com.portofolio.socialMedia.services.OtpService;
import com.portofolio.socialMedia.services.UserService;
import com.portofolio.socialMedia.utils.ApiResponseWrapper;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private OtpService otpService;

    // update name, bio
    @PutMapping("update/biodata")
    public ResponseEntity<ApiResponseWrapper<String>> updateUserBiodata(
            @RequestParam String name,
            @RequestParam String bio) {

        userService.updateUserBiodata(name, bio);

        return ResponseEntity.ok(
                new ApiResponseWrapper<>(
                        "Success update user biodata",
                        null));
    }

    // update photo profile
    @PutMapping("update/photoprofile")
    public ResponseEntity<ApiResponseWrapper<String>> updatePhotoProfile(
            @RequestParam MultipartFile imagefile) {

        userService.updatePhotoProfile(imagefile);

        return ResponseEntity.ok(
                new ApiResponseWrapper<>(
                        "Success updata Photo Profile",
                        null));
    }

    // check apakah username sudah dipakai
    @GetMapping("check/isUsernameExist")
    public ResponseEntity<ApiResponseWrapper<Map<String, Boolean>>> isUsernameExist(
            @RequestParam String newUsername
            ) {

        Map<String, Boolean> result = new HashMap<>();

        String message;
        boolean isUsernameExists = userService.isUsernameExist(newUsername);

        message = "Username is available";
        result.put("isUsernameExist", isUsernameExists);

        return ResponseEntity.ok(new ApiResponseWrapper<>(message, result));
    }

    // update email
    @PostMapping("/verifyEmail")
    public ResponseEntity<ApiResponseWrapper<String>> verifyEmail(
            @RequestParam String otp,
            @RequestParam String email) {

        if (!otpService.verifyOtp(email, otp)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseWrapper<>("otp is not valid", null));
        }

        userService.updateEmail(email);

        return ResponseEntity
                .ok(new ApiResponseWrapper<>("email : " + email + " is valid, success update email", null));
    }

    // update password
    @PutMapping("/update/password")
    public ResponseEntity<ApiResponseWrapper<String>> updatePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {

        userService.updatePassword(oldPassword, newPassword);

        return ResponseEntity.ok(new ApiResponseWrapper<>("Success updating password", null));
    }

    // delete user
    @PostMapping("/delete")
    public ResponseEntity<ApiResponseWrapper<String>> deleteUser() {

        userService.deleteUser();

        return ResponseEntity.ok(new ApiResponseWrapper<>("Success delete user", null));
    }

    // cari user berdasar nama dan username
    @GetMapping("/search")
    public ResponseEntity<ApiResponseWrapper<List<ListUserDTO>>> searchByNameOrUsername(
            @RequestParam String keyword) {
        return ResponseEntity.ok(new ApiResponseWrapper<>("Success query data" , userService.searchByNameOrUsername(keyword)));
    }

    // regis user baru
    @PostMapping("/create")
    public ResponseEntity<ApiResponseWrapper<String>> createUser(
            @RequestBody CreateNewUserDTO createNewUserDTO) {

        userService.createNewUser(createNewUserDTO);

        return ResponseEntity.ok(new ApiResponseWrapper<>("Success Create New User", null));
    }
}
